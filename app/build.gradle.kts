import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

val keyStorePropertiesFile = rootProject.file("keystore.properties")
val useKeyStoreProperties = keyStorePropertiesFile.canRead()
val keyStoreProperties = Properties()

if (useKeyStoreProperties) {
    keyStoreProperties.load(FileInputStream(keyStorePropertiesFile))
}

android {
    if (useKeyStoreProperties) {
        signingConfigs {
            create("config") {
                keyAlias = keyStoreProperties["keyAlias"] as String
                keyPassword = keyStoreProperties["keyPassword"] as String
                storeFile = file(keyStoreProperties["storeFile"] as String)
                storePassword = keyStoreProperties["storePassword"] as String
            }
        }
    }

    namespace = "se.axelkarlsson.hydroxide"
    compileSdk {
        version = release(37) {
            minorApiLevel = 0
        }
    }

    defaultConfig {
        applicationId = "se.axelkarlsson.hydroxide"
        minSdk = 33
        targetSdk = 36
        versionCode = 5
        versionName = versionCode.toString()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }

        release {
            if (useKeyStoreProperties) {
                signingConfig = signingConfigs.getByName("config")
            }

            optimization {
                isMinifyEnabled = true
                isShrinkResources = true
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
}