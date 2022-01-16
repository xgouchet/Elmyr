import fr.xgouchet.buildsrc.settings.commonConfig

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
//    maven
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

commonConfig()
