plugins {
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    kotlin("plugin.serialization") version "2.1.20"
}

android {
    namespace = "dev.logvinovich.inventario"
    compileSdk = 35
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    api(libs.androidx.datastore.preferences)
    api(libs.jwtdecode)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(project(":domain"))
}