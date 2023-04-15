@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.detekt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.junit)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ktlint)
}

android {
    compileSdk = 33
    namespace = "eu.krzdabrowski.currencyadder.core"

    with (defaultConfig) {
        minSdk = 26
        targetSdk = 33
    }

    defaultConfig {
        buildConfigField("String", "NBP_API_URL", "\"https://api.nbp.pl/api/\"")
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            consumerProguardFiles("proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    kotlinOptions {
        freeCompilerArgs = listOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=kotlinx.coroutines.FlowPreview",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.biometric)
    implementation(libs.compose.material3)
    implementation(libs.datastore)
    implementation(libs.hilt)
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.serialization.converter)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.navigation)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.timber)
    androidTestImplementation(libs.bundles.common.android.test)

    kapt(libs.hilt.compiler)
    kaptAndroidTest(libs.test.android.hilt.compiler)

    detektPlugins(libs.detekt.compose.rules)
}
