plugins {
    id("com.android.application")
    id("kotlin-android")
}

val opencvDir: String = System.getenv("OPENCV_ANDROID")

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "com.seramirezdev.document_detection_lib"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk

        testInstrumentationRunner = AppConfig.androidTestInstrumentation

        externalNativeBuild {
            cmake {
                cppFlags("")
                arguments("-DOpenCV_DIR=$opencvDir/sdk/native/jni")
            }
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
        viewBinding = true
    }
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    packagingOptions.pickFirsts.add("**/*.so")
}

dependencies {
    implementation(project(":opencv-lib"))

    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.constraintlayout)
    implementation(Dependencies.material)

    implementation(Dependencies.cameraCore)
    implementation(Dependencies.cameraCamera2)
    implementation(Dependencies.cameraLifecycle)
    implementation(Dependencies.cameraVideo)
    implementation(Dependencies.cameraView)
    implementation(Dependencies.cameraExtensions)

    testImplementation(TestDependencies.junit)
}