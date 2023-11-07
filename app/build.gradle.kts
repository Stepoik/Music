@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("kotlin-kapt")
}

android {
    namespace = "stepan.gorokhov.music"
    compileSdk = 34

    defaultConfig {
        applicationId = "stepan.gorokhov.music"
        minSdk = 24
        targetSdk = 34
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
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

val dagger_version = "2.46.1"
val room_version = "2.5.2"
dependencies {
    implementation(project(path = ":domain"))
    implementation(project(path = ":components"))
    implementation(project(path = ":player_screen"))
    implementation(project(path = ":utils"))
    implementation(project(path = ":scopes"))
    implementation(project(path = ":home_screen"))
    implementation(project(path = ":search_screen"))
    implementation(project(path = ":notifications"))

    implementation(libs.androidx.navigation.compose)

    //Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //Retrofit
    implementation(libs.converter.gson)
    implementation(libs.retrofit)


    //Dagger
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    //UI
    //Async images
    implementation(libs.coil.compose)

    //Compose and other
    implementation(libs.google.accompanist.swiperefresh)
    implementation(libs.material3)
    implementation (libs.accompanist.systemuicontroller)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation("androidx.compose.material:material")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}