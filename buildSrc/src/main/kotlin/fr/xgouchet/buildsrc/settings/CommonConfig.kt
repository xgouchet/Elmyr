package fr.xgouchet.buildsrc.settings

import org.gradle.api.Project

fun Project.commonConfig(projectDescription: String) {
    detektConfig()
    kotlinConfig()
    junitConfig()
    ktLintConfig()
    dokkaConfig()
    mavenConfig(projectDescription)
}
