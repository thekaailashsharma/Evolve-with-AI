pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "dl.bintray.com/jetbrains/markdown")
        maven(url = "https://jitpack.io")
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "dl.bintray.com/jetbrains/markdown")
        maven(url = "https://jitpack.io")
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

rootProject.name = "PalmApi"
include(":app")
