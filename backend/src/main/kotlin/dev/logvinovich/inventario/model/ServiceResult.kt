package dev.logvinovich.inventario.model

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

sealed class ServiceResult<out T> {
    data class Success<out T>(val data: T) : ServiceResult<T>()
    data object NotFound : ServiceResult<Nothing>()
    data object Forbidden : ServiceResult<Nothing>()
    data object BadRequest : ServiceResult<Nothing>()
}

fun <T, R> ServiceResult<T>.toResponseEntity(mapper: ((T) -> R)? = null): ResponseEntity<R> {
    return when (this) {
        ServiceResult.NotFound -> ResponseEntity.notFound().build()
        ServiceResult.Forbidden -> ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        ServiceResult.BadRequest -> ResponseEntity.badRequest().build()
        is ServiceResult.Success -> ResponseEntity.ok(mapper?.let { map -> map(data) })
    }
}