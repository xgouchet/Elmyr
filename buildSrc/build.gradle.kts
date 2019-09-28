import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}


buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.41")
    }
    repositories {
        mavenCentral()
    }
}
apply(plugin = "kotlin")
apply(plugin = "java-gradle-plugin")

repositories {
    mavenCentral()
    maven { setUrl("https://plugins.gradle.org/m2/") }
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.41")
    compile("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.41")
    compile ("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.0.1")
    compile ("org.jlleitschuh.gradle:ktlint-gradle:8.2.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}