object Depends {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val lifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val rxJava = "io.reactivex.rxjava2:rxjava:2.2.7"
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val daggerAndroid = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val daggerProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
    const val annotations = "androidx.annotation:annotation:1.0.0"
    const val room = "androidx.room:room-common:${Versions.room}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomRxJava = "androidx.room:room-rxjava2:${Versions.room}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofitRxJava2Adapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"

    const val junit = "junit:junit:${Versions.junit}"
    const val mockito = "com.nhaarman:mockito-kotlin:${Versions.mockito}"
    const val dexmaker = "com.linkedin.dexmaker:dexmaker-mockito:2.19.0"
    const val roomTesting = "androidx.room:room-testing:${Versions.room}"

    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"

}