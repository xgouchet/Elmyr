package fr.xgouchet.buildsrc.plugin

import java.io.File
import java.io.PrintWriter
import java.nio.charset.Charset
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

open class GithubWikiTask : DefaultTask() {

    init {
        group = "documentation"
        description = "generates a Github compatible wiki reference"

        outputs.upToDateWhen { false }
    }

    private var projectDokkaDir: File = File("./dokka")
    private var projectGithubDir: File = File("./github")
    private var projectName: String = ""
    private var combinedTypes: List<String> = emptyList()

    // region Task

    @TaskAction
    fun applyTask() {
        getOutputDir().mkdirs()
        combinedTypes.forEach {
            combineForType(it)
        }
    }

    @InputDirectory
    internal fun getInputDir(): File {
        val gfmDir = File(projectDokkaDir, "gfm")
        return File(gfmDir, projectName)
    }

    @OutputDirectory
    fun getOutputDir(): File {
        return projectGithubDir
    }

    // endregion

    // region Setters

    fun setDokkaDir(dir: File) {
        projectDokkaDir = dir
    }

    fun setGithubDir(dir: File) {
        projectGithubDir = dir
    }

    fun setTargetName(name: String) {
        projectName = name
    }

    fun setCombinedTypes(types: List<String>) {
        combinedTypes = types
    }

    // endregion

    // region Internal

    private fun combineForType(type: String) {
        val packageDir = File(getInputDir(), type.substringBeforeLast('.'))
        val typeName = type.substringAfterLast('.')
        val typeDir = File(packageDir, convertToDokkaTypeName(typeName))
        val outputFile = File(getOutputDir(), "$typeName.md")

        if (typeDir.exists()) {
            combine(typeDir, outputFile, typeName)
        } else {
            System.err.println("Unable to find $typeDir")
        }
    }

    private fun combine(typeDir: File, outputFile: File, typeName: String) {
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
                if (i > 2) header.add(fixLinks(line, typeName))
            } else {
                val match = indexLinkRegex.matchEntire(line)
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
                    combineSectionFiles(writer, sections[it], it, typeName)
                }
            }
    }

    private fun combineSectionFiles(
        writer: PrintWriter,
        files: List<File>?,
        title: String,
        typeName: String
    ) {
        if (files.isNullOrEmpty()) return

        writer.println("## $title")
        files.forEach {
            appendFile(writer, it, typeName)
        }
    }

    private fun appendFile(writer: PrintWriter, file: File, typeName: String) {
        check(file.exists()) { "Missing file ${file.path}" }
        check(file.canRead()) { "Can't read file ${file.path}" }

        file.forEachLineIndexed { i, line ->
            if (i > 0) {
                if (line.startsWith('#')) {
                    writer.print("##")
                }

                writer.println(fixLinks(line, typeName))
            }
        }
    }

    private fun fixLinks(line: String, typeName: String): String {
        return markdownLinkRegex.replace(line) {
            val title = it.groupValues[1]
            val href = it.groupValues[2]
            if (href.startsWith("http")) {
                it.value
            } else if (href == "#") {
                "[$title]($title)"
            } else if (href == "index.md") {
                "[$title]($typeName)"
            } else {
                val typeHrefMatch = typeHrefRegex.matchEntire(href)
                if (typeHrefMatch != null) {
                    val type = convertFromDokkaTypeName(typeHrefMatch.groupValues[1])
                    val anchor = convertFromDokkaTypeName(typeHrefMatch.groupValues[2])
                    if (anchor == "index") {
                        "[$title]($type)"
                    } else {
                        "[$title]($type#$anchor)"
                    }
                } else {
                    println("••• LINK $title -> $href")
                    "[$title](???)"
                }
            }
        }
    }

    private fun convertToDokkaTypeName(type: String): String {
        val builder = StringBuilder()
        type.forEach {
            when (it) {
                in 'A'..'Z' -> {
                    builder.append('-')
                    builder.append(it.lowercaseChar())
                }
                else -> builder.append(it)
            }
        }
        return builder.toString()
    }

    private fun convertFromDokkaTypeName(type: String): String {
        val builder = StringBuilder()
        var upperCaseNext = false
        type.forEach {
            when (it) {
                '-' -> upperCaseNext = true
                in 'a'..'z' -> {
                    builder.append(if (upperCaseNext) it.lowercaseChar() else it)
                    upperCaseNext = false
                }
                else -> builder.append(it)
            }
        }
        return builder.toString()
    }

    // endregion

    companion object {

        private val sectionNames = listOf(
            "Constructors",
            "Properties",
            "Enum Values",
            "Functions",
            "Companion Object Functions",
            "Companion Object Properties",
            "Types"
        )

        private val indexLinkRegex = Regex("\\| \\[[\\w_\\-&;]+]\\(([\\w_-]+.md)\\) \\| .* \\|")
        private val markdownLinkRegex = Regex("\\[([^]]+)]\\(([^)]+)\\)")
        private val typeHrefRegex = Regex("(?:[\\w.]+/)*(?:([\\w\\-]+)/)?([\\w\\-]+).md")
    }
}

fun File.forEachLineIndexed(charset: Charset = Charsets.UTF_8, action: (Int, String) -> Unit) {
    var index = 0
    forEachLine(charset) {
        action(index, it)
        index++
    }
}
