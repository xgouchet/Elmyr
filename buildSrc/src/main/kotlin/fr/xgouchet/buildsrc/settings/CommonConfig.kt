package fr.xgouchet.buildsrc.settings

import org.gradle.api.Project

fun Project.commonConfig() {
    detektConfig()
    jacocoConfig()
    kotlinConfig()
    junitConfig()
    ktLintConfig()
}
