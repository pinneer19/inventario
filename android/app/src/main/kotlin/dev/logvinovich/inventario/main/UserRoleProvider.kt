package dev.logvinovich.inventario.main

import androidx.compose.runtime.staticCompositionLocalOf
import dev.logvinovich.domain.model.Role

internal val LocalUserRole = staticCompositionLocalOf<Role> { Role.MANAGER }