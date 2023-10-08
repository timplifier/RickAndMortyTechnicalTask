plugins {
    // Application
    id(libs.plugins.agp.library.get().pluginId)

    // Kotlin
    id("kotlin-android")

    // Kapt
    id("kotlin-kapt")

    // Safe Args
    id(libs.plugins.navigation.safeArgs.get().pluginId)
}


android {
    namespace = "com.timplifier.main"
    compileSdk = config.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = config.versions.minSdk.get().toInt()
        targetSdk = config.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName(config.versions.releaseBuildType.get()) {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
    api(project(":core"))

    // Glide
    implementation(libs.glide.glide)
    implementation(libs.paging.rxjava2)

    // Dagger
    kapt(libs.dagger.compiler)
}