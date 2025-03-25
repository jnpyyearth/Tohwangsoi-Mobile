plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.tohwangsoi_mobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tohwangsoi_mobile"
        minSdk = 31
        targetSdk = 35
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
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.inappmessaging)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.car.ui.lib)
    implementation(libs.androidx.scenecore)
    implementation(libs.androidx.navigation.fragment.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform(libs.firebase.bom))

    // Firebase BoM (Bill of Materials)
    implementation(platform(libs.firebase.bom))

    // Firebase core features
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)

    // Realtime Database
    implementation(libs.firebase.database)

    // Cloud Firestore
    implementation(libs.firebase.firestore)

    implementation(libs.firebase.auth)
    implementation(libs.google.auth)

    //ซน
    implementation(libs.google.firebase.storage)
    implementation("com.github.bumptech.glide:glide:4.13.0")
    kapt("com.github.bumptech.glide:compiler:4.13.0")
}