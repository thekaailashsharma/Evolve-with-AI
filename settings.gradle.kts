pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "dl.bintray.com/jetbrains/markdown")
        maven(url = "https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "dl.bintray.com/jetbrains/markdown")
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "PalmApi"
include(":app")
