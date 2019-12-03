package fr.xgouchet.buildsrc.settings

import fr.xgouchet.buildsrc.Dependencies
import java.math.BigDecimal
import org.gradle.api.Project
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport

fun Project.jacocoConfig(targetCoverage: Double) {

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
                    minimum = BigDecimal(targetCoverage)
                }
            }
        }
    }

    tasks.named("check") {
        dependsOn("jacocoTestReport")
        dependsOn("jacocoTestCoverageVerification")
    }
}
