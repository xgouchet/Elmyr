package fr.xgouchet.buildsrc.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.PrintWriter
import java.nio.charset.Charset

open class GithubWikiTask : DefaultTask() {

    init {
        group = "documentation"
        description = "generates a Github compatible wiki reference"
    }

    internal var projectDokkaDir: File = File("./dokka")
    internal var projectGithubDir: File = File("./github")
    internal var projectName: String = ""

    internal var extension: GithubWikiExtension = GithubWikiExtension()


    // region Task

    @TaskAction
    fun applyTask() {
        getOutputDir().mkdirs()
        extension.types.forEach {
            combineForType(it)
        }
    }

    @InputDirectory
    fun getInputDir(): File {
        return File(projectDokkaDir, projectName)
    }

    @OutputDirectory
    fun getOutputDir(): File {
        return projectGithubDir
    }

    // endregion

    // region Internal

    private fun combineForType(type: String) {
        val packageDir = File(getInputDir(), type.substringBeforeLast('.'))
        val typeDir = File(packageDir, convertTypeName(type.substringAfterLast('.')))
        val outputFile = File(getOutputDir(), "${type.substringAfterLast('.')}.md")

        if (typeDir.exists()) {
            combine(typeDir, outputFile)
        } else {
            System.err.println("Unable to find $typeDir")
        }
    }

    private fun convertTypeName(type: String): String {
        val builder = StringBuilder()
        type.forEach {
            when (it) {
                in 'A'..'Z' -> {
                    builder.append('-')
                    builder.append(it.toLowerCase())
                }
                else -> builder.append(it)
            }
        }
        return builder.toString()
    }

    private fun combine(typeDir: File, outputFile: File) {
        val indexFile = File(typeDir, "index.md")

        val header = mutableListOf<String>()
        val sections = mutableMapOf<String, MutableList<File>>().apply {
            sectionNames.forEach { put(it, mutableListOf()) }
        }


        var fillList: MutableList<File>? = null
        indexFile.forEachLineIndexed { i: Int, line: String ->
            if (line.startsWith("### ")) {
                val key = line.substring(4)
                if (key != "Parameters") {
                    check(sections.containsKey(key)) { "Unknown section \"$key\" for type ${typeDir.name}" }
                    fillList = sections[key]
                }
            }

            if (fillList == null) {
                if (i > 2) header.add(line)
            } else {
                val match = linkRegex.matchEntire(line)
                if (match != null) {
                    fillList?.add(File(typeDir, match.groupValues[1]))
                }
            }
        }

        outputFile.printWriter(Charsets.UTF_8)
                .use { writer ->
                    header.forEach {
                        writer.println(it)
                    }

                    sectionNames.forEach {
                        combineSectionFiles(writer, sections[it], it)
                    }
                }
    }

    private fun combineSectionFiles(
            writer: PrintWriter,
            files: List<File>?,
            title: String) {
        if (files.isNullOrEmpty()) return

        writer.println("## $title")
        files.forEach {
            appendFile(writer, it)
        }
    }

    private fun appendFile(writer: PrintWriter, file: File) {
        check(file.exists()) { "Missing file ${file.path}" }
        check(file.canRead()) { "Can't read file ${file.path}" }

        file.forEachLineIndexed { i, line ->
            if (i > 0) {
                if (line.startsWith('#')) {
                    writer.print("##")
                }
                writer.println(line)
            }
        }
    }

    // endregion

    companion object {

        private val sectionNames = listOf(
                "Constructors",
                "Properties",
                "Enum Values",
                "Functions",
                "Types"
        )

        private const val LINK_PATTERN = "\\| \\[[\\w_\\-&;]+\\]\\(([\\w_-]+.md)\\) \\| .* \\|"

        private val linkRegex = Regex(LINK_PATTERN)

    }
}


fun File.forEachLineIndexed(charset: Charset = Charsets.UTF_8, action: (Int, String) -> Unit) {
    var index = 0
    forEachLine(charset) {
        action(index, it)
        index++
    }
}