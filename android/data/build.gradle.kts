plugins {
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    kotlin("plugin.serialization") version "2.1.20"
}

android {
    namespace = "dev.logvinovich.inventario.data"
    compileSdk = 35

    defaultConfig {
        buildConfigField("String", "SERVER_HOST", "\"${project.properties["server_host"]}\"")
        buildConfigField(
            "int",
            "SERVER_PORT",
            project.properties["server_port"]?.toString() ?: "8080"
        )
    }

    buildFeatures {
        buildConfig = true
    }
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
    implementation(libs.ktor.client.websockets)

    implementation("org.slf4j:slf4j-android:1.7.25")
    implementation("io.ktor:ktor-client-logging:3.1.2")
    api(libs.androidx.datastore.preferences)
    api(libs.jwtdecode)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(project(":domain"))
}