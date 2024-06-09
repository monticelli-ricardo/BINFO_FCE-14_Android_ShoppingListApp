plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs.kotlin")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // ViewModel
    implementation(libs.viewmodel)
    implementation(libs.viewmodel.savedstate)
    // LiveData
    implementation(libs.livedata)
    // Lifecycles
    implementation(libs.lifecycle)
    implementation(libs.lifecycle.compose)
    kapt(libs.lifecycle.compiler)

    // ROOM Database
    implementation(libs.room)
    kapt(libs.room.compiler)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    // Coroutines
    implementation(libs.coroutines)
    implementation(libs.room.ktx)

    // Dagger
    implementation("com.google.dagger:dagger:2.40")
    kapt("com.google.dagger:dagger-compiler:2.40")
}
