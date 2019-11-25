package fr.xgouchet.buildsrc.settings

import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaTask

fun Project.dokkaConfig() {

    tasks.withType(DokkaTask::class.java) {
        outputFormat = "gfm"
//        outputDirectory = "${rootDir.path}/../Elmyr.wiki/"
    }
}
