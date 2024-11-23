plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
}

android {
    namespace = "ru.topbun.toursadmin"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.topbun.toursadmin"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    debugImplementation(libs.accompanist.systemuicontroller)
    debugImplementation(libs.coil)
    debugImplementation(libs.coil.compose)
    debugImplementation(libs.kotlinx.serialization.json)

    debugImplementation(libs.voyager.bottom.sheet.navigator)
    debugImplementation(libs.voyager.navigator)
    debugImplementation(libs.voyager.screenmodel)
    debugImplementation(libs.voyager.tab.navigator)
    debugImplementation(libs.voyager.transitions)

    debugImplementation(libs.androidx.appcompat)
    debugImplementation(libs.material)
    debugImplementation(libs.androidx.runtime.android)
    debugImplementation(libs.ktor.client.cio)
    debugImplementation(libs.ktor.client.core)
    debugImplementation(libs.ktor.negotiation)
    debugImplementation(libs.ktor.kotlinx.serialization.json)
    debugImplementation(libs.ktor.logging.android)
}