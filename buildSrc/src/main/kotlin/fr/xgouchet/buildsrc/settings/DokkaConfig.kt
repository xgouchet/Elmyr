package fr.xgouchet.buildsrc.settings

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.DokkaTask

@Suppress("UnstableApiUsage")
fun Project.dokkaConfig() {

    tasks.register("generateJavadoc", Jar::class.java) {
        dependsOn(":dd-sdk-android:dokka")
        archiveClassifier.convention("javadoc")
        from("${buildDir.canonicalPath}/reports/javadoc")
    }

    tasks.withType(DokkaTask::class.java) {
        outputFormat = "gfm"
        configuration {
            includeNonPublic = false
            skipDeprecated = false
            reportUndocumented = true
            skipEmptyPackages = true
            // targets = listOf("JVM")
        }
//        outputDirectory = "${rootDir.path}/../Elmyr.wiki/"
    }
}
