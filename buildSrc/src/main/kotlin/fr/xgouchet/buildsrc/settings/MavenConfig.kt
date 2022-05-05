package fr.xgouchet.buildsrc.settings

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.get

fun Project.mavenConfig() {
    extensionConfig<PublishingExtension> {
        publications.create("maven", MavenPublication::class.java) {
            groupId = "com.github.xgouchet"
            artifactId = project.name
            version = "1.3.4"
            from(components["kotlin"])
            artifact(tasks.findByName("kotlinSourcesJar"))
            artifact(tasks.findByName("generateJavadoc"))
        }
    }
}