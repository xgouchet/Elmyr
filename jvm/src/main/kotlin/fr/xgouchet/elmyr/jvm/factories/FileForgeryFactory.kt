package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import java.io.File
import java.lang.IllegalStateException
import kotlin.math.min

/**
 * A [ForgeryFactory] that will generate a [File] instance.
 */
class FileForgeryFactory :
        ForgeryFactory<File> {

    override fun getForgery(forge: Forge): File {
        return File(aLocalPath(forge))
    }

    private fun aLocalPath(
        forge: Forge,
        absolute: Boolean? = null
    ): String {
        val osName = System.getProperty("os.name").toLowerCase()
        return if (osName.contains("win")) {
            aWindowsPath(forge, absolute)
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            aLinuxPath(forge, absolute)
        } else if (osName.contains("mac")) {
            aMacOsPath(forge, absolute)
        } else {
            throw IllegalStateException("Unsupported OS path format for “$osName”")
        }
    }

    private fun aLinuxPath(
        forge: Forge,
        absolute: Boolean? = null
    ): String {
        val isAbsolute = absolute ?: forge.aBool()
        val ancestorRoot = Array(forge.aTinyInt()) { ".." }.joinToString(UNIX_SEP) { it }
        val roots = if (isAbsolute) LINUX_ROOTS else listOf(".", "..", ancestorRoot)
        return aPath(
                forge = forge,
                separator = UNIX_SEP,
                roots = roots,
                forbiddenChars = UNIX_FORBIDDEN_CHARS
        )
    }

    private fun aWindowsPath(
        forge: Forge,
        absolute: Boolean? = null
    ): String {
        val isAbsolute = absolute ?: forge.aBool()
        val ancestorRoot = Array(forge.aTinyInt()) { ".." }.joinToString(WINDOWS_SEP) { it }
        val roots = if (isAbsolute) WINDOWS_ROOTS else listOf(".", "..", ancestorRoot)
        return aPath(
                forge = forge,
                separator = WINDOWS_SEP,
                roots = roots,
                forbiddenChars = WINDOWS_FORBIDDEN_CHARS,
                reservedFilenames = WINDOWS_RESERVED_FILENAMES
        )
    }

    private fun aMacOsPath(
        forge: Forge,
        absolute: Boolean? = null
    ): String {
        val isAbsolute = absolute ?: forge.aBool()
        val ancestorRoot = Array(forge.aTinyInt()) { ".." }.joinToString(UNIX_SEP) { it }
        val roots = if (isAbsolute) MAC_ROOTS else listOf(".", "..", ancestorRoot)
        return aPath(
                forge = forge,
                separator = UNIX_SEP,
                roots = roots,
                forbiddenChars = UNIX_FORBIDDEN_CHARS
        )
    }

    private fun aPath(
        forge: Forge,
        separator: String = File.separator,
        roots: List<String>,
        forbiddenChars: CharArray,
        reservedFilenames: List<String>? = null
    ): String {
        val builder = StringBuilder()
        var segments = 0

        if (roots.isNotEmpty()) {
            builder.append(forge.anElementFrom(roots))
                    .append(separator)
            segments++
        }

        val isFile = forge.aBool()
        val fileSize = if (isFile) forge.anInt(3, MAX_FILENAME_SIZE) else 0
        val maxSize = (MAX_PATH_SIZE - fileSize - separator.length)
        val reserved = reservedFilenames ?: emptyList()

        while ((builder.length < maxSize) or !forge.aBool(segments.toFloat() / 10.0f)) {
            val max = min(MAX_FILENAME_SIZE, maxSize - builder.length - separator.length)
            if (max <= 1) break

            val segmentSize = forge.anInt(1, max)
            // TODO maybe extend the charset to full UTF 8 ?
            buildSegment(forge, segmentSize, forbiddenChars, reserved, builder)
            builder.append(separator)
            segments++
        }

        if (isFile) {
            if (fileSize > 5) {
                val extSize = forge.anInt(1, 4)
                val baseNameSize = fileSize - extSize - 1
                buildSegment(forge, baseNameSize, forbiddenChars, reserved, builder)
                builder.append('.')
                builder.append(forge.anAlphabeticalString(size = extSize))
            }
        }

        return builder.toString()
    }

    private fun buildSegment(
        forge: Forge,
        segmentSize: Int,
        forbiddenChars: CharArray,
        reserved: List<String>,
        builder: StringBuilder
    ) {
        var segment: String
        do {
            segment = forge.aString(size = segmentSize) {
                var c = forge.anAsciiChar()
                while (c in forbiddenChars) {
                    c = forge.anAsciiChar()
                }
                c
            }
        } while (segment in reserved)

        builder.append(segment)
    }

    companion object {

        private const val MAX_PATH_SIZE = 1024
        private const val MAX_FILENAME_SIZE = 128

        private val LINUX_ROOTS = listOf("", "/bin", "/boot", "/dev", "/dev/null", "/etc", "/home", "/lib", "/media",
                "/mnt", "/opt", "/sbin", "/srv", "/tmp", "/usr", "/usr/bin", "/usr/lib", "/usr/share",
                "/usr/local", "/usr/local/bin", "/var", "/var/lib", "/var/log", "/root", "/sys")
        private val MAC_ROOTS = listOf("", "/Applications", "/Developer", "/Library", "/Network", "/System", "/Users",
                "/Volumes", "/bin", "/dev", "/dev/null", "/etc", "/sbin", "/tmp", "/usr", "/usr/bin",
                "/usr/lib", "/usr/share", "/usr/local", "/usr/local/bin", "/var", "/var/lib", "/var/log")
        private val WINDOWS_ROOTS = listOf("A:", "C:", "D:", "C:\\Program Files", "C:\\Program Files (x86)",
                "C:\\Program Files\\Common Files", "C:\\ProgramData", "C:\\Users", "C:\\Users\\Public", "C:\\Documents and Settings",
                "C:\\Windows", "C:\\Windows\\System32")

        private const val UNIX_SEP = "/"
        private const val WINDOWS_SEP = "\\"

        private val UNIX_FORBIDDEN_CHARS = arrayOf(0.toChar(), '/').toCharArray()
        private val WINDOWS_FORBIDDEN_CHARS = IntArray(32) { it }.map { it.toChar() }
                .union(listOf('<', '>', ':', '"', '/', '\\', '|', '?', '.'))
                .toCharArray()

        private val WINDOWS_RESERVED_FILENAMES = listOf("CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "COM4",
                "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6",
                "LPT7", "LPT8", "LPT9")
    }
}
