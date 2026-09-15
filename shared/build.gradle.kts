import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    id("com.google.devtools.ksp")
}

kotlin {
    // 1. Target dla Androida
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // 2. Target dla aplikacji Desktop / CLI (czysta JVM)
    jvm("desktop")

    // 3. Konfiguracja źródeł wspóldzielonych (commonMain)
    sourceSets {
        commonMain.dependencies {
            // Tutaj dodajemy biblioteki wieloplatformowe (np. Coroutines)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.room.runtime)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
    add("kspCommonMainMetadata", libs.androidx.room.compiler)
}

android {
    namespace = "pl.wluczak.myexpenses.shared"
    compileSdk = 35
    defaultConfig {
        minSdk = 29
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}