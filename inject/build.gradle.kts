import fr.xgouchet.buildsrc.settings.commonConfig

plugins {
    kotlin("jvm")
    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

dependencies {

    implementation(project(":core"))

    implementation(libs.kotlin)
    implementation(libs.kotlinReflect)

    testImplementation(libs.bundles.junit5)
    testImplementation(libs.bundles.spek)
    testImplementation(libs.assertJ)
    testImplementation(libs.junit4)
    testImplementation(libs.mockitoKotlin)
    testImplementation(libs.mockitoJunit5)
}

commonConfig("Injection tool for Elmyr.")
