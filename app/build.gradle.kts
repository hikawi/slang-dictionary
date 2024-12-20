/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.10/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val version = "2.0"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
    implementation(libs.guava)
    implementation("com.formdev:flatlaf:3.5.2")
    implementation("com.formdev:flatlaf-intellij-themes:3.5.2")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }

    sourceCompatibility = JavaVersion.VERSION_21;
    targetCompatibility = JavaVersion.VERSION_21;
}

application {
    mainClass.set("dev.frilly.slangdict.Application")
}

tasks.jar {
    manifest {
        attributes["Manifest-Version"] = "1.0"
        attributes["Main-Class"] = "dev.frilly.slangdict.Application"
    }
    archiveBaseName.set("SlangDictionary")
    archiveVersion.set(version)
    archiveClassifier.set("")
}

tasks.shadowJar {
    archiveBaseName.set("SlangDictionary") // 出力JARの名前
    archiveVersion.set(version) // バージョン
    archiveClassifier.set("full") // ファイル名に「all」を追加
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
