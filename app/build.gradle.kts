plugins {

    // Application
    id(libs.plugins.agp.application.get().pluginId)

    // Kotlin
    id("kotlin-android")

    // Kapt
    id("kotlin-kapt")

    // Navigation SafeArgs
    id(libs.plugins.navigation.safeArgs.get().pluginId)
}

android {
    namespace = "com.timplifier.rickandmortytechnicaltask"
    compileSdk = config.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.timplifier.rickandmortytechnicaltask"
        minSdk = config.versions.minSdk.get().toInt()
        targetSdk = config.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName(config.versions.releaseBuildType.get()) {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        getByName(config.versions.debugBuildType.get()) {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = jvmOptions.versions.kotlinJvmTargetOptions.get()
    }

    //ViewBinding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":features:main"))

    // UI Components
    implementation(libs.bundles.uiComponents)

    // Core
    implementation(libs.android.core)

    // Lifecycle
    implementation(libs.bundles.lifecycle)

    // Navigation
    implementation(libs.bundles.navigation)

    // Dagger
    kapt(libs.dagger.compiler)
}