plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id(libs.plugins.kotlin.serialization.get().pluginId)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
dependencies {
    api((project(":common")))
    api(libs.javax.inject)
    api(libs.coroutines.core)
    implementation(libs.coroutines.test)
    implementation(libs.paging.common)
    implementation(libs.ktor.serialization.kotlinx.json)

    testImplementation(libs.google.truth)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
}