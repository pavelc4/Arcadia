plugins {
    id("com.android.application") version "9.0.0"
    id("org.jetbrains.kotlin.android") version "1.9.23"
}

android {
    namespace = "com.android.arcadia"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.android.arcadia"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        // buildConfigField("Boolean", "IS_SYSTEM_BUILD", "false") // Comment out for now
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        buildConfig = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11" // Compatible with Kotlin 1.9.23
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src") 
            res.srcDirs("res")
            manifest.srcFile("AndroidManifest.xml")
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-service:2.10.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")


    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.ui:ui-graphics:1.6.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.0")
    implementation("androidx.compose.material3:material3:1.4.0")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    debugImplementation("androidx.compose.ui:ui-tooling:1.6.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.0")
}