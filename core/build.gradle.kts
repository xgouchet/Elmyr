import fr.xgouchet.buildsrc.Dependencies
import fr.xgouchet.buildsrc.testCompile
import fr.xgouchet.buildsrc.compile

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    jacoco
}

dependencies {

    compile(Dependencies.Libraries.Kotlin)

    testCompile(Dependencies.Libraries.JUnit5)
    testCompile(Dependencies.Libraries.Spek)
    testCompile(Dependencies.Libraries.TestTools)
}

apply(from = "${project.rootDir}/script/gradle/junit.settings.gradle")
apply(from = "${project.rootDir}/script/gradle/kotlin.settings.gradle")
apply(from = "${project.rootDir}/script/gradle/detekt.settings.gradle")
apply(from = "${project.rootDir}/script/gradle/ktlint.settings.gradle")
apply(from = "${project.rootDir}/script/gradle/jacoco.settings.gradle")
