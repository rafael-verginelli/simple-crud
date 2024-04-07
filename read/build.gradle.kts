plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.rafver.read"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    // Allow references to generated code
    kapt {
        correctErrorTypes = true
    }

    hilt {
        enableAggregatingTask = false
    }
}

dependencies {

    implementation(project(":core_ui"))
    implementation(project(":core_data"))
    implementation(project(":core_domain"))
    implementation(project(":core_testing"))

    // Hilt
    implementation(libs.hilt)
    kapt(libs.hilt.kapt.compiler)

    // Tests
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kluent)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

}