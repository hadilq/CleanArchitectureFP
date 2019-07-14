plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(Versions.compileSdk)
    defaultConfig {
        applicationId = "com.github.hadilq.cleanarchitecturefp"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        setTargetCompatibility(1.8)
        setSourceCompatibility(1.8)
    }
}

dependencies {
    kapt(Depends.daggerCompiler)
    kapt(Depends.daggerProcessor)
    kapt(Depends.lifecycleCompiler)
    kapt(Depends.roomCompiler)

    kaptAndroidTest(Depends.daggerCompiler)
    kaptAndroidTest(Depends.daggerProcessor)

    implementation(project(":domain"))
    implementation(project(":data"))
//    implementation(project(":presentation_common"))
//    implementation(project(":presentation_artists"))
//    implementation(project(":presentation_albums"))
//    implementation(project(":presentation_album"))

    implementation(Depends.kotlin)
    implementation(Depends.appcompat)
    implementation(Depends.lifecycle)
    implementation(Depends.constraintLayout)
    implementation(Depends.dagger)
    implementation(Depends.daggerAndroid)
    implementation(Depends.rxJava)
    implementation(Depends.room)
    implementation(Depends.roomRuntime)

    testImplementation(Depends.junit)

    androidTestImplementation(Depends.testRunner)
    androidTestImplementation(Depends.espressoCore)
}