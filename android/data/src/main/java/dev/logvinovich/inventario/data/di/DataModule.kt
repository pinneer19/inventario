package dev.logvinovich.inventario.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.logvinovich.inventario.data.BuildConfig
import dev.logvinovich.inventario.data.JwtManager
import dev.logvinovich.inventario.data.api.ChatWebSocketClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [BindsModule::class])
object DataModule {
    private const val SERVER_HOST = BuildConfig.SERVER_HOST
    private const val SERVER_PORT = BuildConfig.SERVER_PORT

    @Provides
    @Singleton
    fun provideHttpClient(jwtManager: JwtManager) = HttpClient(CIO) {
        defaultRequest {
            host = SERVER_HOST
            port = SERVER_PORT
            url {
                protocol = URLProtocol.Companion.HTTP
            }
            contentType(ContentType.Application.Json)
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 3000L
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.SIMPLE
        }
        install(Auth) {
            bearer {
                sendWithoutRequest { request ->
                    !request.url.encodedPath.startsWith("/user")
                }
                loadTokens {
                    jwtManager.getTokens()
                }
                refreshTokens {
                    jwtManager.refreshToken()
                }
            }
        }
        install(WebSockets) {
            pingIntervalMillis = 10_000
        }
    }

    @Provides
    @Singleton
    fun provideChatWebSocketClient(
        httpClient: HttpClient,
        jwtManager: JwtManager
    ): ChatWebSocketClient {
        return ChatWebSocketClient(
            httpClient = httpClient,
            jwtManager = jwtManager,
            baseUrl = "ws://$SERVER_HOST:$SERVER_PORT/ws/chat"
        )
    }
}