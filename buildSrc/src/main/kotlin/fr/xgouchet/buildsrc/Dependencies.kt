package fr.xgouchet.buildsrc

object Dependencies {

    object Versions {
        // Commons
        const val Kotlin = "1.3.50"

        // Tests
        const val JUnit4 = "4.12"
        const val JUnitJupiter = "5.5.2"
        const val JUnitPlatform = "1.5.2"
        const val JUnitVintage = "5.5.2"

        const val Spek = "1.2.1"
        const val AssertJ = "0.2.1"
        const val Mockito = "1.6.0"

        // Tools
        const val Detekt = "1.0.0-RC16"
        const val DependencyVersion = "0.21.0"
    }

    object Libraries {

        const val Kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.Kotlin}"

        const val JUnit4 = "junit:junit:${Versions.JUnit4}"

        @JvmField val Spek = arrayOf(
                "org.jetbrains.spek:spek-api:${Versions.Spek}",
                "org.jetbrains.spek:spek-subject-extension:${Versions.Spek}",
                "org.jetbrains.spek:spek-junit-platform-engine:${Versions.Spek}",
                "org.jetbrains.kotlin:kotlin-reflect:${Versions.Kotlin}"
        )

        @JvmField val JUnit5 = arrayOf(
                "org.junit.platform:junit-platform-launcher:${Versions.JUnitPlatform}",
                "org.junit.vintage:junit-vintage-engine:${Versions.JUnitVintage}",
                "org.junit.jupiter:junit-jupiter:${Versions.JUnitJupiter}"
        )

        @JvmField val TestTools = arrayOf(
                "net.wuerl.kotlin:assertj-core-kotlin:${Versions.AssertJ}",
                "com.nhaarman:mockito-kotlin:${Versions.Mockito}"
        )

    }

    object ClassPaths {
        const val Kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin}"

        const val JUnitPlatform = "org.junit.platform:junit-platform-gradle-plugin:${Versions.JUnitPlatform}"
    }

    object Repositories {
        const val Fabric = "https://maven.fabric.io/public"
        const val Jitpack = "https://jitpack.io"
        const val Gradle = "https://plugins.gradle.org/m2/"
        const val Google = "https://maven.google.com"
    }

    object PluginNamespaces {
        const val Detetk = "io.gitlab.arturbosch"
        const val DependencyVersion = "com.github.ben-manes"
        const val Kotlin = "org.jetbrains.kotlin"
    }
}
