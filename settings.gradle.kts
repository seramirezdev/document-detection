@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "document-detection"
include(":document-detection-lib")
include(":document-detection-app")

val opencvDir: String = System.getenv("OPENCV_ANDROID")
include(":opencv-lib")
project(":opencv-lib").projectDir = File("${opencvDir}/sdk")