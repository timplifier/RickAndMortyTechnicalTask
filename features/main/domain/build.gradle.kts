plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
dependencies {
    api((project(":common")))
    api(libs.javax.inject)
    api(libs.bundles.rxjava)
    api(libs.coroutines.core)
    implementation(libs.paging.common)
}