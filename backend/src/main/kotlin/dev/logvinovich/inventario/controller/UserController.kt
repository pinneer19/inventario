package dev.logvinovich.inventario.controller

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import dev.logvinovich.inventario.entity.User
import dev.logvinovich.inventario.model.TokenRequest
import dev.logvinovich.inventario.model.TokenResponse
import dev.logvinovich.inventario.security.JwtUtil
import dev.logvinovich.inventario.service.user.UserService
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val service: UserService,
    private val jwtUtil: JwtUtil,
    @Value("\${google-client-id}") private val googleClientId: String,
) {
    @PostMapping("/login")
    fun login(@RequestBody request: User): ResponseEntity<TokenResponse> {
        val user = service.getUser(request)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val response = jwtUtil.generateTokenResponse(user)

        return ResponseEntity.ok(response)
    }

    @PostMapping("/register")
    fun register(@RequestBody request: User): ResponseEntity<TokenResponse> {
        val user = service.insertIntoDatabase(request)
        val response = jwtUtil.generateTokenResponse(user)

        return ResponseEntity.ok(response)
    }

    @PostMapping("/google")
    fun googleAuth(@RequestBody token: TokenRequest): ResponseEntity<TokenResponse> {
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(googleClientId))
            .build()

        val idToken = verifier.verify(token.token)

        if (idToken != null) {
            val payload = idToken.payload
            val email = payload.email

            val user = service.findOrInsert(
                User(username = email)
            )
            val response = jwtUtil.generateTokenResponse(user)

            return ResponseEntity.ok(response)
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody request: TokenRequest): ResponseEntity<TokenResponse> {
        val refreshToken = request.token
        println("REFRESHING")
        return try {
            val tokenType = jwtUtil.extractClaim(refreshToken) { claims -> claims["type"] as? String }

            if (tokenType != "refresh") {
                return ResponseEntity.badRequest().build()
            }

            val username = jwtUtil.extractUsername(refreshToken)
            val isValid = jwtUtil.validateToken(refreshToken, username)

            if (!isValid) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

            val user = service.findByUsername(username)
                ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

            val response = jwtUtil.generateTokenResponse(user)

            return ResponseEntity.ok(response)
        } catch (ex: SignatureException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                TokenResponse(error = "Invalid JWT")
            )
        }
    }

    companion object {
        private const val TOKEN_RESPONSE = "token"
    }
}