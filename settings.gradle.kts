pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "dl.bintray.com/jetbrains/markdown")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "dl.bintray.com/jetbrains/markdown")
    }
}

rootProject.name = "PalmApi"
include(":app")
