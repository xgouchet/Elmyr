package fr.xgouchet.buildsrc.settings

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

fun Project.junitConfig() {
    tasks.withType(Test::class.java) {
        useJUnitPlatform {
            includeEngines("spek", "junit-jupiter", "junit-vintage")
        }
    }
}