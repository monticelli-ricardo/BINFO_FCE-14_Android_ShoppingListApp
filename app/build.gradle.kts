plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs")
    kotlin("kapt")
}

android {
    namespace = "com.example.shoppinglistapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.shoppinglistapp"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.compose.preview.renderer)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // ViewModel
    implementation(libs.viewmodel)
    // ViewModel utilities for Compose
    implementation(libs.viewmodel.compose)
    // LiveData
    implementation(libs.livedata)
    // Lifecycles only (without ViewModel or LiveData)
    implementation(libs.lifecycle)
    // Lifecycle utilities for Compose
    implementation(libs.lifecycle.compose)
    // Annotation processor
    annotationProcessor(libs.lifecycle.compiler)
    // ROOM Database
    implementation(libs.room)
    annotationProcessor(libs.room.compiler)
    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    // Coroutines
    implementation(libs.coroutines)
    implementation (libs.room.ktx)
}