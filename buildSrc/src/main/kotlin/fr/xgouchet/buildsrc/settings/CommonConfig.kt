package fr.xgouchet.buildsrc.settings

import org.gradle.api.Project

fun Project.commonConfig(targetCoverage: Double = 0.95) {
    detektConfig()
    jacocoConfig(targetCoverage)
    kotlinConfig()
    junitConfig()
    ktLintConfig()
    dokkaConfig()
}
