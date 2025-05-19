package dev.logvinovich.inventario.security

import dev.logvinovich.inventario.entity.User
import dev.logvinovich.inventario.model.TokenResponse
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import java.util.function.Function
import javax.crypto.SecretKey

@Component
class JwtUtil (
    @Value("\${security.jwt.secret-key}") private val secretKey: String,
    @Value("\${security.jwt.expiration-time}") private val jwtExpiration: Long,
    @Value("\${security.jwt.refresh-expiration-time}") private val jwtRefreshExpiration: Long
) {
    private val signingKey: SecretKey
        get() {
            val keyBytes: ByteArray = Decoders.BASE64.decode(secretKey)
            return Keys.hmacShaKeyFor(keyBytes)
        }

    fun generateToken(user: User, refresh: Boolean = false): String {
        val now = System.currentTimeMillis()
        val expiration = if (refresh) jwtRefreshExpiration else jwtExpiration

        return Jwts.builder()
            .subject(user.username)
            .claim("role", user.role)
            .claim("userId", user.id)
            .claim("type", if (refresh) "refresh" else "access")
            .issuedAt(Date(now))
            .expiration(Date(now + expiration))
            .signWith(signingKey)
            .compact()
    }

    fun validateToken(token: String, user: String): Boolean {
        return try {
            val username = extractUsername(token)
            val expirationDate = extractClaim(token, Claims::getExpiration)
            return username == user && expirationDate.after(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun extractUsername(token: String): String = extractClaim(token, Claims::getSubject)

    fun generateTokenResponse(user: User): TokenResponse {
        return TokenResponse(generateToken(user), generateToken(user, refresh = true))
    }
}
