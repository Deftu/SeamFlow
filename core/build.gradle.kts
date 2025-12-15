import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("dev.deftu.gradle.tools")
}

kotlin {
    jvm("desktop") {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }
        }

        withSourcesJar()
    }

    androidTarget()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.outcome)
            implementation(libs.bundles.kotlinx.coroutines)
            implementation(libs.bundles.kotlinx.serialization)
            implementation(libs.bundles.ktor.client)
            implementation(libs.bundles.ktor.serialization)
            implementation(libs.bundles.ktor.network)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.logging)

            implementation(libs.ktor.server.core)
        }

        val desktopMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.logging)

                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.cio)

                implementation(libs.oshi.core)

            }
        }

        val desktopTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

android {
    namespace = "dev.deftu.seamflow.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
