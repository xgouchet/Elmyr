package fr.xgouchet.elmyr.annotation

/**
 * The type of String to be forged.
 */
enum class StringForgeryType {
    /** Will forge a String with only ASCII printable characters. */
    ASCII,
    /** Will forge a String with only ASCII Extended printable characters. */
    ASCII_EXTENDED,
    /** Will forge a String with only alphabetical characters. */
    ALPHABETICAL,
    /** Will forge a String with only alphabetical or numerical characters. */
    ALPHA_NUMERICAL,
    /** Will forge a String with only hexadecimal characters (A to F and digits). */
    HEXADECIMAL,
    /** Will forge a String with only numerical characters. */
    NUMERICAL,
    /** Will forge a String with only whitespace characters. */
    WHITESPACE
}