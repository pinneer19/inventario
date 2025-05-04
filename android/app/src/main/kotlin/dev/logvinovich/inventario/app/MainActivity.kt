package dev.logvinovich.inventario.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import dev.logvinovich.inventario.splash.viewmodel.TokenViewModel
import dev.logvinovich.inventario.ui.theme.InventarioTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenViewModel by viewModels<TokenViewModel>()

        installSplashScreen().setKeepOnScreenCondition {
            tokenViewModel.uiState.value.loading
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
        )

        setContent {
            InventarioTheme {
                val uiState by tokenViewModel.uiState.collectAsState()
                if (!uiState.loading) {
                    AppNavGraph(uiState)
                }
            }
        }
    }
}
