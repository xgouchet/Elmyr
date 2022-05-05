package fr.xgouchet.buildsrc.settings

import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar

@Suppress("UnstableApiUsage")
fun Project.dokkaConfig() {

    project.tasks.register("generateJavadoc", Jar::class.java) {
        group = "publishing"
        dependsOn("dokkaJavadoc")
        archiveClassifier.convention("javadoc")
        from("${buildDir.canonicalPath}/reports/javadoc")
    }
}
