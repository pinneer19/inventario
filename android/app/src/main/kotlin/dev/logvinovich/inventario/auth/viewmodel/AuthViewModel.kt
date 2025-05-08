package dev.logvinovich.inventario.auth.viewmodel

import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logvinovich.data.JwtManager
import dev.logvinovich.domain.model.AuthToken
import dev.logvinovich.domain.model.Role
import dev.logvinovich.domain.usecase.auth.GoogleAuthUseCase
import dev.logvinovich.domain.usecase.auth.LoginUseCase
import dev.logvinovich.domain.usecase.auth.RegisterUseCase
import dev.logvinovich.inventario.auth.util.getUserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val googleAuthUseCase: GoogleAuthUseCase,
    private val jwtManager: JwtManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun handleIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.GoogleAuth -> googleAuth(intent.credentialResponse)

            is AuthIntent.Login -> with(_uiState.value) {
                if (!validateInputFields(username, password)) return

                authenticate {
                    loginUseCase(username, password)
                }
            }

            is AuthIntent.Register -> with(_uiState.value) {
                if (!validateInputFields(username, password)) return

                authenticate {
                    registerUseCase(username, password, selectedRole)
                }
            }

            is AuthIntent.UpdateUsername -> updateUsername(intent.username)

            is AuthIntent.UpdatePassword -> updatePassword(intent.password)

            is AuthIntent.ClearUsername -> updateUsername("")

            is AuthIntent.TogglePasswordVisibility -> togglePasswordVisibility()

            is AuthIntent.ToggleUserRole -> toggleUserRole()
        }
    }

    private fun validateInputFields(username: String, password: String): Boolean {
        when {
            username.isEmpty() -> {
                _uiState.update { it.copy(usernameError = true) }
                return false
            }
            password.isEmpty() -> {
                _uiState.update { it.copy(passwordError = true) }
                return false
            }
        }
        return true
    }

    private fun googleAuth(result: GetCredentialResponse) {
        val credential = result.credential

        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)

                        val idToken = googleIdTokenCredential.idToken
                        authenticate { googleAuthUseCase(idToken) }
                    } catch (_: GoogleIdTokenParsingException) {
                        _uiState.update {
                            it.copy(error = "Received an invalid google id token response")
                        }
                    }
                }
            }
        }
    }

    private fun authenticate(request: suspend () -> Result<AuthToken>) {
        _uiState.update {
            it.copy(loading = true, error = null)
        }

        viewModelScope.launch {
            val response = request()
            if (response.isSuccess) {
                val (accessToken, refreshToken) = response.getOrThrow()
                jwtManager.saveTokens(accessToken, refreshToken)

                _uiState.update {
                    it.copy(
                        authenticated = true,
                        loading = false,
                        selectedRole = getUserRole(accessToken)
                    )
                }
            } else {
                _uiState.update {
                    it.copy(error = response.exceptionOrNull()?.message, loading = false)
                }
            }
        }
    }

    private fun updateUsername(username: String) {
        _uiState.update {
            it.copy(username = username)
        }
    }

    private fun updatePassword(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    private fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(passwordVisible = !it.passwordVisible)
        }
    }

    private fun toggleUserRole() {
        _uiState.update {
            when (it.selectedRole) {
                Role.MANAGER -> it.copy(selectedRole = Role.ADMIN)
                Role.ADMIN -> it.copy(selectedRole = Role.MANAGER)
            }
        }
    }
}