package dev.logvinovich.inventario.data.api

import dev.logvinovich.inventario.data.model.auth.AuthTokenDto
import dev.logvinovich.inventario.domain.model.Role
import dev.logvinovich.inventario.data.model.auth.TokenRequest
import dev.logvinovich.inventario.data.model.auth.UserAuthRequest
import dev.logvinovich.inventario.data.util.apiRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

suspend inline fun HttpClient.googleAuth(token: String): Result<AuthTokenDto> {
    return apiRequest {
        post("/user/google") {
            setBody(TokenRequest(token))
        }
    }
}

suspend inline fun HttpClient.register(
    username: String,
    password: String,
    role: Role
): Result<AuthTokenDto> {
    return apiRequest {
        post("/user/register") {
            setBody(UserAuthRequest(username, password, role))
        }
    }
}

suspend inline fun HttpClient.login(username: String, password: String): Result<AuthTokenDto> {
    return apiRequest {
        post("/user/login") {
            setBody(UserAuthRequest(username, password))
        }
    }
}

suspend inline fun HttpClient.refreshAccessToken(refreshToken: String): Result<AuthTokenDto> {
    return apiRequest {
        post("/user/refresh") {
            setBody(TokenRequest(refreshToken))
        }
    }
}