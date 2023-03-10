plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "com.seramirezdev.document_detection_app"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
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
    packagingOptions.pickFirsts.add("**/*.so")
}

dependencies {

    implementation(files("./libs/document-detection-lib-release.aar"))
    implementation(project(":opencv-lib"))

    implementation(Dependencies.cameraCore)
    implementation(Dependencies.cameraCamera2)
    implementation(Dependencies.cameraLifecycle)
    implementation(Dependencies.cameraVideo)
    implementation(Dependencies.cameraExtensions)
    implementation(Dependencies.cameraView)

    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.material)
    implementation(Dependencies.constraintlayout)
    testImplementation(TestDependencies.junit)
}