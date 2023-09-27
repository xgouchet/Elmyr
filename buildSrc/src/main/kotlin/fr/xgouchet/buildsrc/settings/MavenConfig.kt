package fr.xgouchet.buildsrc.settings

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.get
import org.gradle.plugins.signing.SigningExtension

const val GROUP_NAME = "fr.xgouchet.elmyr"
const val VERSION = "1.4.0"

fun Project.mavenConfig(projectDescription: String) {

    group = GROUP_NAME
    version = VERSION

    val publishingExtension = extensionConfig<PublishingExtension> {
        publications.create("maven", MavenPublication::class.java) {
            groupId = GROUP_NAME
            artifactId = project.name
            version = VERSION

            from(components["kotlin"])
            artifact(tasks.findByName("kotlinSourcesJar"))
            artifact(tasks.findByName("generateJavadoc"))

            pom {
                name.set(project.name)
                description.set(projectDescription)
                url.set("https://github.com/xgouchet/Elmyr/")

                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/license/mit/")
                    }
                }
                developers {
                    developer {
                        name.set("Xavier F. Gouchet")
                        email.set("github@xgouchet.fr")
                        roles.set(listOf("Author", "Maintainer"))
                    }
                }

                scm {
                    url.set("https://github.com/xgouchet/Elmyr/")
                    connection.set("scm:git:git@github.com:xgouchet/Elmyr.git")
                    developerConnection.set("scm:git:git@github.com:xgouchet/Elmyr.git")
                }
            }
        }
    }

    if (publishingExtension != null) {
        extensionConfig<SigningExtension> {
//            val privateKey =project.properties["ossrhUsername"]?.toString()
//            val password = System.getenv("GPG_PASSWORD")
//            useInMemoryPgpKeys(privateKey, password)
            sign(publishingExtension.publications.getByName("maven"))
        }
    }
}