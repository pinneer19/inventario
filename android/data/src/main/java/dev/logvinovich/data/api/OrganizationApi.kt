package dev.logvinovich.data.api

import dev.logvinovich.data.model.OrganizationDto
import dev.logvinovich.data.util.apiRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post

suspend inline fun HttpClient.getAdminUserOrganizations(): Result<List<OrganizationDto>> {
    return apiRequest {
        get("/organizations")
    }
}

suspend inline fun HttpClient.createOrganization(
    organizationName: String
): Result<OrganizationDto> {
    return apiRequest {
        post("/organizations") {
            parameter("organizationName", organizationName)
        }
    }
}