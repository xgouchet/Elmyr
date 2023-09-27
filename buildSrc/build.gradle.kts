import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
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
    implementation(libs.kotlin)
    implementation(libs.kotlinGP)
    implementation(libs.detektGP)
    implementation(libs.depsVersionGP)
    implementation(libs.dokkaGP)
    implementation(libs.ktlintGP)
    implementation(libs.nexusPublishGP)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
}

gradlePlugin {
    plugins {
        register("githubWiki") {
            id = "githubWiki" // the alias
            implementationClass = "fr.xgouchet.buildsrc.plugin.GithubWikiPlugin"
        }
    }
}
