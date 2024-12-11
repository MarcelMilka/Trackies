plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

//  Firebase
    id("com.google.gms.google-services")

//  Hilt
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.trackies"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.trackies"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.trackies.di.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
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
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

//  Compose navigation API
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.7")
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")
    implementation("androidx.navigation:navigation-compose:2.7.7")

//  Extended library of Google icons
    implementation("androidx.compose.material:material-icons-extended:1.7.3")

//  Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.2")
    implementation("androidx.navigation:navigation-compose:2.8.2")
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation ("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-database")

//  Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // For Robolectric tests.

//  Testing dependency injection with hilt

//      For Robolectric tests.
        testImplementation("com.google.dagger:hilt-android-testing:2.51.1")
        kaptTest("com.google.dagger:hilt-android-compiler:2.51.1")

//      For instrumented tests.
        androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
        kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.51.1")

//  MVVM
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

//  Lifecycle-aware components
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")

//  Room
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    testImplementation("androidx.room:room-testing:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-guava:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

//  Mockk
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.mockk.agent)
    //noinspection UseTomlInstead
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.0.0")

//  UI testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.5")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.5")

}

kapt {
    correctErrorTypes = true
}