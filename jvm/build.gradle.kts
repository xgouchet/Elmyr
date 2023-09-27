import fr.xgouchet.buildsrc.settings.commonConfig

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

dependencies {

    implementation(project(":core"))

    testImplementation(project(":inject"))
    testImplementation(project(":junit5"))

    testImplementation(libs.bundles.junit5)
    testImplementation(libs.assertJ)
    testImplementation(libs.mockitoJunit5)
    testImplementation(libs.mockitoKotlin)
}

commonConfig("JVM standard classes factories for Elmyr, providing fuzzy testing and property based testing features.")
