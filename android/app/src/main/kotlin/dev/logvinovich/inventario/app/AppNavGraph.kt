package dev.logvinovich.inventario.app

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.logvinovich.inventario.R
import dev.logvinovich.inventario.auth.ui.AuthGraph
import dev.logvinovich.inventario.auth.ui.authNavigation
import dev.logvinovich.inventario.main.MainGraph
import dev.logvinovich.inventario.main.mainNavigation
import dev.logvinovich.inventario.splash.viewmodel.TokenUiState
import dev.logvinovich.inventario.ui.util.ObserveAsEvents
import dev.logvinovich.inventario.ui.util.SnackbarController
import dev.logvinovich.inventario.ui.util.network.NetworkConnectionState
import dev.logvinovich.inventario.ui.util.network.NoConnectionScreen
import dev.logvinovich.inventario.ui.util.network.rememberConnectivityState
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavGraph(
    tokenState: TokenUiState,
    navController: NavHostController = rememberNavController()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val connectionState by rememberConnectivityState()
    val hasNetworkConnection by remember(connectionState) {
        derivedStateOf {
            connectionState === NetworkConnectionState.Available
        }
    }

    ObserveAsEvents(flow = SnackbarController.events, snackbarHostState) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = event.message ?: context.getString(requireNotNull(event.messageRes)),
                actionLabel = event.action?.name,
                duration = SnackbarDuration.Short
            )

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }

    LaunchedEffect(connectionState) {
        when (connectionState) {
            is NetworkConnectionState.Lost -> {
                SnackbarController.sendMessageResEvent(R.string.lost_internet_connection)
            }

            is NetworkConnectionState.Unavailable -> {
                SnackbarController.sendMessageResEvent(R.string.internet_connection_unavailable)
            }

            is NetworkConnectionState.Available -> {}
        }
    }

    val startDestination = if (tokenState.authenticated == true && tokenState.userData != null) {
        MainGraph(Json.encodeToString(tokenState.userData))
    } else {
        AuthGraph
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        AnimatedContent(targetState = hasNetworkConnection) { target ->
            if (!target) {
                NoConnectionScreen()
            } else {
                val floatAnimationSpec: FiniteAnimationSpec<IntOffset> = tween(500)
                val intAnimationSpec: FiniteAnimationSpec<Float> = tween(400)

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = floatAnimationSpec
                        ) + fadeIn(animationSpec = intAnimationSpec)
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = floatAnimationSpec
                        ) + fadeOut(animationSpec = intAnimationSpec)
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = floatAnimationSpec
                        ) + fadeIn(animationSpec = intAnimationSpec)
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = floatAnimationSpec
                        ) + fadeOut(animationSpec = intAnimationSpec)
                    }
                ) {

                    authNavigation(navController)

                    mainNavigation(navController)
                }
            }
        }
    }
}