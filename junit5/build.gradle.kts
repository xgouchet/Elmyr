import fr.xgouchet.buildsrc.Dependencies
import fr.xgouchet.buildsrc.testCompile
import fr.xgouchet.buildsrc.compileOnly

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.github.ben-manes.versions")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    jacoco
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

apply(from = "${project.rootDir}/script/gradle/junit.settings.gradle")
apply(from = "${project.rootDir}/script/gradle/kotlin.settings.gradle")
apply(from = "${project.rootDir}/script/gradle/detekt.settings.gradle")
apply(from = "${project.rootDir}/script/gradle/ktlint.settings.gradle")
apply(from = "${project.rootDir}/script/gradle/jacoco.settings.gradle")
