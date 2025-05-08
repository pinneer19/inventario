package dev.logvinovich.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.auth0.android.jwt.DecodeException
import com.auth0.android.jwt.JWT
import dagger.Lazy
import dev.logvinovich.data.api.refreshAccessToken
import dev.logvinovich.data.model.auth.AuthTokenDto
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.first
import java.util.Date
import javax.inject.Inject

class JwtManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val httpClient: Lazy<HttpClient>
) {
    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { prefs ->
            prefs[JWT_ACCESS_TOKEN] = accessToken
            prefs[JWT_REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun hasValidRefreshToken(): Boolean {
        val prefs = dataStore.data.first()
        val token = prefs[JWT_REFRESH_TOKEN] ?: return false

        return try {
            val jwt = JWT(token)
            jwt.expiresAt?.after(Date()) == true
        } catch (_: DecodeException) {
            false
        }
    }

    suspend fun getTokens(): BearerTokens? {
        val prefs = dataStore.data.first()
        val accessToken = prefs[JWT_ACCESS_TOKEN]
        val refreshToken = prefs[JWT_REFRESH_TOKEN]
        return if (accessToken != null && refreshToken != null) {
            BearerTokens(accessToken, refreshToken)
        } else null
    }

    suspend fun getRefreshToken(): String? {
        return dataStore.data.first()[JWT_REFRESH_TOKEN]
    }

    suspend fun refreshToken(): BearerTokens? {
        val refreshToken = getRefreshToken() ?: return null
        return try {
            val response: Result<AuthTokenDto> = httpClient.get().refreshAccessToken(refreshToken)

            val (accessToken, refreshToken) = response.getOrThrow()

            saveTokens(accessToken, refreshToken)

            BearerTokens(accessToken, refreshToken)
        } catch (_: Exception) {
            null
        }
    }

    companion object {
        private val JWT_ACCESS_TOKEN = stringPreferencesKey("jwt_access_token")
        private val JWT_REFRESH_TOKEN = stringPreferencesKey("jwt_refresh_token")
    }
}