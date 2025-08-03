plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    id("kotlin-parcelize")
    //nav component
    alias(libs.plugins.navigation.safeargs)
}

android {
    buildFeatures{
        viewBinding= true
    }
    namespace = "com.example.bazar"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.bazar"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //retrofit
    implementation(libs.retrofit2.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.google.gson)
    //OkHTTP client
    implementation(libs.okhttp3.okhttp)
    implementation(libs.logging.interceptor)

    //coil
    implementation(libs.coil)
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.androidx.room.compiler)


    implementation(libs.androidx.navigation.compose)

    implementation(libs.cokoin)
    implementation(libs.cokoin.android.viewmodel) // for Androidx ViewModel
    implementation(libs.cokoin.android.navigation) // for Compose Navigation
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.lottie.compose)
// برای ViewModel و Runtime
    implementation(libs.androidx.navigation.runtime.ktx)
}