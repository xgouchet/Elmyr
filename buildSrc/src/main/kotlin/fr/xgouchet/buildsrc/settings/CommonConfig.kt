package fr.xgouchet.buildsrc.settings

import org.gradle.api.Project

fun Project.commonConfig() {
    detektConfig()
    kotlinConfig()
    junitConfig()
    ktLintConfig()
    dokkaConfig()
    mavenConfig()
}
