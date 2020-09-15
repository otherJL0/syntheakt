plugins {
    java
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    id("org.jetbrains.dokka") version "1.4.0"
}

group = "com.syntheakt"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-core", "1.0.0-RC")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-properties","1.0.0-RC")
    testImplementation("junit", "junit", "4.12")
    testImplementation("org.jetbrains.kotlin", "kotlin-test-annotations-common", "1.4.0")
    testImplementation("org.jetbrains.kotlin", "kotlin-test-common", "1.4.0")
    testImplementation("org.jetbrains.kotlin", "kotlin-test-junit", "1.4.0")
    testImplementation("io.mockk", "mockk-common", "1.10.0")
    testImplementation("io.mockk", "mockk-dsl", "1.10.0")
    testImplementation("io.mockk", "mockk", "1.10.0")
}
