plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.rafver.details"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
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

    implementation(project(libs.versions.coreUi.get()))
    implementation(project(libs.versions.coreData.get()))
    implementation(project(libs.versions.coreDomain.get()))
    implementation(project(libs.versions.coreTesting.get()))

    implementation(project(libs.versions.featureCreate.get()))
    
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