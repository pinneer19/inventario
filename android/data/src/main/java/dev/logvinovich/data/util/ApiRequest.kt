package dev.logvinovich.data.util

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

suspend inline fun <reified T> HttpClient.apiRequest(
    block: suspend HttpClient.() -> HttpResponse
): Result<T> {
    return try {
        val response = block()

        when (response.status) {
            HttpStatusCode.OK -> {
                val body = response.body<T>()
                Result.success(body)
            }

            else -> {
                Result.failure(Exception("Unexpected status code: ${response.status}"))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}