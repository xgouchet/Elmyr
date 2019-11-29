import fr.xgouchet.buildsrc.Dependencies
import fr.xgouchet.buildsrc.settings.commonConfig
import fr.xgouchet.buildsrc.testCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.dokka")
    jacoco
    maven
}

dependencies {

    compile(project(":core"))

    testCompile(Dependencies.Libraries.JUnit5)
    testCompile(Dependencies.Libraries.TestTools)
    testCompile(Dependencies.Libraries.JUnit5Extensions)

    testCompile(project(":junit5"))
}

commonConfig()
