@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "2.0.21"
//    alias(libs.plugins.room)
}

android {
    namespace = "com.ashraf.vidown"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ashraf.vidown"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        jniLibs.useLegacyPackaging = true
    }

//    ksp {
//        arg("room.incremental", "true")
//    }
//
//    room {
//        schemaDirectory("$projectDir/schemas")
//    }

    androidResources {
        generateLocaleConfig = true
    }
}

dependencies {

    /* ---------------- Core ---------------- */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    /* ---------------- Compose ---------------- */
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidxCompose)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /* ---------------- Navigation ---------------- */
    implementation(libs.androidx.navigation.compose)

    /* ---------------- Dependency Injection (Hilt) ---------------- */
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    /* ---------------- Database (Room) ---------------- */
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    /* ---------------- Kotlin / Coroutines ---------------- */
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    /* ---------------- Downloader stack ---------------- */
    implementation(libs.bundles.youtubedlAndroid)

    /* ---------------- Testing ---------------- */
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.testing)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}