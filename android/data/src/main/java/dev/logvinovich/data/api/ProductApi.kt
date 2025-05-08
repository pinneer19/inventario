package dev.logvinovich.data.api

import dev.logvinovich.data.model.inventory.ProductDto
import dev.logvinovich.data.util.apiRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json

suspend fun HttpClient.getOrganizationProducts(organizationId: Long): Result<List<ProductDto>> {
    return apiRequest {
        get("/products") {
            parameter("organizationId", organizationId)
        }
    }
}

suspend fun HttpClient.saveProduct(
    productDto: ProductDto,
    image: ByteArray?
): Result<ProductDto> {
    val multipartData = MultiPartFormDataContent(
        formData {
            append("product", Json.encodeToString(productDto), headers = Headers.build {
                append(HttpHeaders.ContentType, "application/json")
            })
            image?.let {
                append(
                    key = "productImage",
                    value = it,
                    headers = Headers.build {
                        append(
                            HttpHeaders.ContentDisposition,
                            "filename=image.jpg"
                        )
                        append(HttpHeaders.ContentType, "image/jpeg")
                    }
                )
            }
        }
    )
    return apiRequest {
        productDto.id?.let {
            put("/products/$it") {
                setBody(multipartData)
            }
        } ?: post("/products") {
            setBody(multipartData)
        }
    }
}

suspend fun HttpClient.deleteProductById(id: Long): Result<Unit> {
    return apiRequest {
        delete("/products/$id")
    }
}