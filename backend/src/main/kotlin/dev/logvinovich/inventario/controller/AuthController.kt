package dev.logvinovich.inventario.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/test-auth")
    fun testAuth(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(listOf("ab", "vc"))
    }
}