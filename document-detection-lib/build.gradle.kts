plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = LibConfig.compileSdk

    defaultConfig {
        minSdk = LibConfig.minSdk
        targetSdk = LibConfig.targetSdk

        testInstrumentationRunner = LibConfig.androidTestInstrumentation

        externalNativeBuild {
            cmake {
                cppFlags("")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {

    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appCompat)
    testImplementation(TestDependencies.junit)
}