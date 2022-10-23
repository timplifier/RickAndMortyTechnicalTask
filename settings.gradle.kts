pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()

        maven { url = uri("https://jitpack.io") }
    }
}

enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven("tim-plifier")
        maven { url = uri("https://jitpack.io") }
    }
    versionCatalogs {
        create("config") {
            from(files("gradle/config.versions.toml"))
        }
        create("options") {
            from(files("gradle/options.versions.toml"))
        }
    }
}

rootProject.name = "Rick And Morty Technical Task"
include(":app", ":common", ":core")
include(":features:main",":features:main:data",":features:main:domain")