plugins {
    `java-library`
}

dependencies {
    implementation(Depends.kotlin)
    implementation(Depends.rxJava)

    testImplementation(Depends.junit)
    testImplementation(Depends.mockito)
}