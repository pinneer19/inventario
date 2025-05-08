package dev.logvinovich.inventario.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            try {
                val username = jwtUtil.extractUsername(token)

                if (SecurityContextHolder.getContext().authentication == null) {
                    val user = userDetailsService.loadUserByUsername(username)
                    if (user != null && jwtUtil.validateToken(token, user.username)) {
                        val auth = UsernamePasswordAuthenticationToken(
                            user, null, user.authorities
                        )
                        SecurityContextHolder.getContext().authentication = auth
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is ExpiredJwtException -> response.writer.write("JWT token expired")
                    is JwtException -> response.writer.write("Invalid JWT token")
                }
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                return
            }
        }

        filterChain.doFilter(request, response)
    }
}