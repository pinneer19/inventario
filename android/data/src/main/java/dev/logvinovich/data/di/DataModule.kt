package dev.logvinovich.data.di

import android.R.attr.level
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.logvinovich.data.JwtManager
import dev.logvinovich.data.repository.OrganizationRepositoryImpl
import dev.logvinovich.domain.repository.OrganizationRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.logging.Logger
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [BindsModule::class])
object DataModule {
    @Provides
    @Singleton
    fun provideHttpClient(jwtManager: JwtManager) = HttpClient(CIO) {
        defaultRequest {
            host = "192.168.1.6"
            port = 8080
            url {
                protocol = URLProtocol.Companion.HTTP
            }
            contentType(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
//        install(Logging){
//            level = LogLevel.ALL
//            logger = io.ktor.client.plugins.logging.Logger.SIMPLE
//        }
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
    }
}