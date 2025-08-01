plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.assignmentthree"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.assignmentthree"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // Set the test instrumentation runner
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.places)
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:19.1.0")

    // Testing dependencies
    testImplementation(libs.junit) // For unit tests
    androidTestImplementation(libs.ext.junit) // For AndroidX instrumentation tests
    androidTestImplementation(libs.espresso.core) // For Espresso tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:rules:1.5.0") // For ActivityTestRule
    // Mocking library for unit tests
    testImplementation("org.mockito:mockito-core:4.0.0")
    testImplementation("junit:junit:4.13.2")

    // Add the Places SDK for testing
    androidTestImplementation("com.google.android.libraries.places:places:latest_version")

    // Add MockWebServer for instrumentation tests
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.2")
}
