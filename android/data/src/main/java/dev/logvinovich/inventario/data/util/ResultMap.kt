package dev.logvinovich.inventario.data.util

inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when {
        this.isSuccess -> Result.success(transform(this.getOrNull()!!))
        else -> Result.failure(this.exceptionOrNull()!!)
    }
}