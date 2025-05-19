package dev.logvinovich.inventario.auth.ui.login

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import dev.logvinovich.inventario.BuildConfig
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.auth.ui.AuthScreen
import dev.logvinovich.inventario.auth.viewmodel.AuthIntent
import dev.logvinovich.inventario.auth.viewmodel.AuthViewModel
import dev.logvinovich.inventario.ui.util.SnackbarController
import dev.logvinovich.inventario.ui.util.SnackbarEvent
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onNavigateToMain: (String) -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.server_client_id)
        .setAutoSelectEnabled(true)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    val credentialManager = CredentialManager.create(context)
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.authenticated, uiState.error) {
        if (uiState.authenticated) onNavigateToMain(uiState.userData.toJson())
        else if (uiState.error != null) {
            SnackbarController.sendEvent(
                SnackbarEvent(message = context.getString(R.string.wrong_password))
            )
        }
    }

    Scaffold { contentPadding ->
        AuthScreen(
            title = stringResource(R.string.login),
            username = uiState.username,
            password = uiState.password,
            isLoading = uiState.loading,
            isPasswordVisible = uiState.passwordVisible,
            toggleAuthScreen = stringResource(R.string.navigate_to_register),
            onUpdateUsername = {
                viewModel.handleIntent(AuthIntent.UpdateUsername(it))
            },
            onUpdatePassword = {
                viewModel.handleIntent(AuthIntent.UpdatePassword(it))
            },
            onClearUsername = {
                viewModel.handleIntent(AuthIntent.ClearUsername)
            },
            togglePasswordVisibility = {
                viewModel.handleIntent(AuthIntent.TogglePasswordVisibility)
            },
            onAuthToggle = onNavigateToRegister,
            onSubmit = {
                viewModel.handleIntent(AuthIntent.Login)
            },
            modifier = Modifier.padding(contentPadding),
            onGoogleSignIn = {
                coroutineScope.launch {
                    try {
                        val result = credentialManager.getCredential(
                            request = request,
                            context = context,
                        )
                        viewModel.handleIntent(AuthIntent.GoogleAuth(result))
                    } catch (_: GetCredentialException) {
                        SnackbarController.sendEvent(
                            SnackbarEvent(message = context.getString(R.string.wrong_credentials))
                        )
                    }
                }
            },
            error = uiState.error
        )
    }
}
