import java.io.FileInputStream
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.test.palmapi"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.test.palmapi"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            val localProperties = Properties()
            localProperties.load(FileInputStream(rootProject.file("local.properties")))
            buildConfigField("String", "API_KEY", "${localProperties["API_KEY"]}")
        }
        release {
            val localProperties = Properties()
            localProperties.load(FileInputStream(rootProject.file("local.properties")))
            buildConfigField("String", "API_KEY", "${localProperties["API_KEY"]}")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    ksp {

    }
    kapt {
        correctErrorTypes = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //Initial
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)

    //Material 3
    implementation(libs.material3)

    //Ktor Client
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.gson)
    implementation(libs.ktor.client.cio)

    //ViewModel Compose
    implementation(libs.viewmodel.compose)

    //Navigation Compose
    implementation(libs.accompanist.navigation)

    // Material Icons Extended
    implementation(libs.material.icons.extended)

    //Firebase
    implementation(platform(libs.firebase.bom))

    //Coil
    implementation(libs.coilx)

    //Room
    implementation(libs.room.runtime)
    implementation("com.google.firebase:firebase-dynamic-links-ktx:21.1.0")
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    //Dagger Hilt
    implementation(libs.dagger.hilt)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.auth)
    kapt(libs.dagger.hilt.kapt)
    implementation(libs.dagger.hilt.navigation)

    //Lottie Animation
    implementation(libs.lottie)

    //DataStore
    implementation(libs.datastore.core)

    //Permissions
    implementation(libs.permissions)

    //CameraX
    implementation(libs.cameraX.core)
    implementation(libs.cameraX.lifecycle)
    implementation(libs.cameraX.view)
    implementation(libs.cameraX.camera2)
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)

    //MlKit
    implementation(libs.mlkit.barcode)

    //TextRecognition
    implementation(libs.play.services.mlkit.text.recognition.common)
    implementation(libs.play.services.mlkit.text.recognition) //LatinScript

    //Keyboard
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.splitties.systemservices)
    implementation(libs.splitties.views)
    implementation(libs.androidx.appcompat)

    //Firebase
    implementation(libs.firebase.messaging.ktx)

    //Work Manager
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    //Test Android
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}