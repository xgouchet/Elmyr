import fr.xgouchet.buildsrc.Dependencies
import fr.xgouchet.buildsrc.testCompile
import fr.xgouchet.buildsrc.compileOnly
import fr.xgouchet.buildsrc.settings.commonConfig

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    jacoco
    maven
}

dependencies {

    compile(project(":core"))
    compile(project(":inject"))
    compile(Dependencies.Libraries.Kotlin)

    compileOnly(Dependencies.Libraries.JUnit5)

    testCompile(Dependencies.Libraries.JUnit5)
    testCompile(Dependencies.Libraries.Spek)
    testCompile(Dependencies.Libraries.TestTools)
    testCompile(Dependencies.Libraries.JUnit5Extensions)
}

commonConfig()