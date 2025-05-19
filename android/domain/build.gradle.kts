plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    kotlin("plugin.serialization") version "2.1.20"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.jakarta.inject.api)
    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.datetime)
}