buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
    }
    repositories {
        google()
        mavenCentral()
        maven(url = "dl.bintray.com/jetbrains/markdown")
    }

}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    id("com.google.dagger.hilt.android") version "2.45" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    id("org.jmailen.kotlinter") version "3.13.0" apply false
    id("com.android.library") version "8.1.0" apply false
}
true // Needed to make the Suppress annotation work for the plugins block

subprojects {
    apply(plugin = "org.jmailen.kotlinter") // Version should be inherited from parent
}