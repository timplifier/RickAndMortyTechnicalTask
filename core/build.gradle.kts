plugins {
    // Application
    id("com.android.library")

    // Kotlin
    id("kotlin-android")

    // Kapt
    id("kotlin-kapt")

    // Navigation SafeArgs
    id(libs.plugins.navigation.safeArgs.get().pluginId)
}

android {
    namespace = "com.timplifier.core"
    compileSdk = config.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = config.versions.minSdk.get().toInt()
        targetSdk = config.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = jvmOptions.versions.kotlinJvmTargetOptions.get()
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    api(project(":common"))
    api(project(":features:main:data"))

    // UI Components
    api(libs.bundles.uiComponents)

    //Glide
    implementation(libs.glide.glide)

    // Core
    api(libs.android.core)

    // Coroutines
    api(libs.bundles.coroutines)
    api(libs.bundles.rxjava.android)

    // Lifecycle
    api(libs.bundles.lifecycle)

    // Navigation
    api(libs.bundles.navigation)

    // Dagger
    api(libs.dagger.dagger)
    kapt(libs.dagger.compiler)
}