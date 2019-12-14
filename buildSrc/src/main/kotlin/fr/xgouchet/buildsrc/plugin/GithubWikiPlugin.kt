package fr.xgouchet.buildsrc.plugin

import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project

class GithubWikiPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val extension = target.extensions
                .create(EXT_NAME, GithubWikiExtension::class.java)

        val buildTask = target.tasks
                .create(TASK_NAME, GithubWikiTask::class.java)
        buildTask.projectDokkaDir = File(target.buildDir, "dokka")
        buildTask.projectGithubDir = File("${target.rootDir.path}/../${target.rootProject.name}.wiki")
        buildTask.projectName = target.name
        buildTask.extension = extension

        target.tasks.named("dokka") {
            buildTask.dependsOn(this)
        }
    }

    companion object {
        const val EXT_NAME = "githubWiki"

        const val TASK_NAME = "githubWiki"
    }
}
