package dev.logvinovich.inventario.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logvinovich.data.JwtManager
import dev.logvinovich.inventario.auth.util.getUserRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val jwtManager: JwtManager
): ViewModel() {
    private val _uiState = MutableStateFlow(TokenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getLocalJwtToken()
    }

    private fun getLocalJwtToken() {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            val token = jwtManager.getRefreshToken()
            val isTokenValid = jwtManager.hasValidRefreshToken()

            val userRole = if (isTokenValid && token != null) getUserRole(token) else null

            _uiState.update {
                it.copy(
                    loading = false,
                    authenticated = isTokenValid,
                    userRole = userRole
                )
            }
        }
    }
}