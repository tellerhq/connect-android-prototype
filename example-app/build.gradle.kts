plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 31
    buildToolsVersion = "30.0.3"

    namespace = "io.teller.connect"

    defaultConfig {
        applicationId = "io.teller.connect.example"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

val fragmentVersion = "1.4.1"

dependencies {
    implementation(project(":sdk"))
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("androidx.fragment:fragment:$fragmentVersion")
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")
    implementation("androidx.webkit:webkit:1.4.0")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.8.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}