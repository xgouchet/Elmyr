package fr.xgouchet.buildsrc.settings

import fr.xgouchet.buildsrc.Dependencies
import org.gradle.api.Project
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport

fun Project.jacocoConfig() {

    extensionConfig<JacocoPluginExtension> {
        toolVersion = Dependencies.Versions.Jacoco
        reportsDir = file("$buildDir/jacoco") // Jacoco's output root.
    }

    tasks.withType(JacocoReport::class.java) {
        reports {
            csv.isEnabled = false
            xml.isEnabled = true
            html.isEnabled = true
            html.destination = file("$buildDir/jacoco/html")
        }
    }

    tasks.withType(JacocoCoverageVerification::class.java) {
        violationRules {
            rule {
                limit {
                    minimum = 0.85.toBigDecimal()
                }
            }
        }
    }

    tasks.named("check") {
        dependsOn("jacocoTestReport")
        dependsOn("jacocoTestCoverageVerification")
    }
}
