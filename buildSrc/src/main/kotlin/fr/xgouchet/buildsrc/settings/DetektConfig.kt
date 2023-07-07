package fr.xgouchet.buildsrc.settings

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project

fun Project.detektConfig() {

    extensionConfig<DetektExtension> {
        version = "1.0.1"

        source = files("$projectDir/src/main/kotlin")
        config = files("${project.rootDir}/script/config/detekt.yml")
//        reports {
//            xml {
//                enabled = true
//                destination = file("build/reports/detekt.xml")
//            }
//        }
    }

    tasks.named("check") {
        dependsOn("detekt")
    }
}
