package fr.xgouchet.elmyr

@JvmField
val LINUX_ROOTS = listOf("", "/bin", "/boot", "/dev", "/dev/null", "/etc", "/home", "/lib", "/media",
        "/mnt", "/opt", "/sbin", "/srv", "/tmp", "/usr", "/usr/bin", "/usr/lib", "/usr/share",
        "/usr/local", "/usr/local/bin", "/var", "/var/lib", "/var/log", "/root", "/sys")
@JvmField
val MAC_ROOTS = listOf("", "/Applications", "/Developer", "/Library", "/Network", "/System", "/Users",
        "/Volumes", "/bin", "/dev", "/dev/null", "/etc", "/sbin", "/tmp", "/usr", "/usr/bin",
        "/usr/lib", "/usr/share", "/usr/local", "/usr/local/bin", "/var", "/var/lib", "/var/log")
@JvmField
val WINDOWS_ROOTS = listOf("A:", "C:", "D:", "C:\\Program Files", "C:\\Program Files (x86)",
        "C:\\Program Files\\Common Files", "C:\\ProgramData", "C:\\Users", "C:\\Users\\Public", "C:\\Documents and Settings",
        "C:\\Windows", "C:\\Windows\\System32")


@JvmField
val UNIX_SEP = '/'

@JvmField
val WINDOWS_SEP = '\\'

@JvmField
val LINUX_FORBIDDEN_CHARS = arrayOf(0.toChar(), UNIX_SEP).toCharArray()

@JvmField
val WINDOWS_FORBIDDEN_CHARS = IntArray(32) { it }.map { it.toChar() }.union(listOf('<', '>', ':', '"', '/', '\\', '|', '?', '.')).toCharArray()

@JvmField
val WINDOWS_RESERVED_FILENAMES = listOf("CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "COM4",
        "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6",
        "LPT7", "LPT8", "LPT9")