plugins {
    // Application
    id(libs.plugins.agp.library.get().pluginId)

    // Kotlin
    id("kotlin-android")

    // Kapt
    id("kotlin-kapt")
    id(libs.plugins.kotlin.serialization.get().pluginId)
}


android {
    namespace = "com.timplifier.data"
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

        getByName(config.versions.debugBuildType.get()) {
            buildConfigField("String", "BASE_URL", "\"https://rickandmortyapi.com/\"")
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
    api(project(":features:main:domain"))

    // OkHttp
    implementation(libs.bundles.okHttp)

    // Ktor
    implementation(libs.bundles.ktor)

    // Gson
    implementation(libs.google.gson)

    // Paging
    api(libs.paging.paging)

    // Room
    api(libs.bundles.room)
    kapt(libs.room.compiler)

    // Dagger
    implementation(libs.dagger.dagger)
    kapt(libs.dagger.compiler)
}