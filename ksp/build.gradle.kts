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
    implementation(libs.kotlinSP)

    implementation("com.squareup:kotlinpoet:1.14.2") {
        exclude(module = "kotlin-reflect")
    }
    implementation("com.squareup:kotlinpoet-ksp:1.14.2")

    testImplementation(project(":inject"))
    testImplementation(project(":junit5"))

    testImplementation(libs.bundles.junit5)
    testImplementation(libs.assertJ)
    testImplementation(libs.mockitoJunit5)
    testImplementation(libs.mockitoKotlin)
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.5.0")
}

commonConfig("KSP project")
