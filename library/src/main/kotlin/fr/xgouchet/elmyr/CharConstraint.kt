package fr.xgouchet.elmyr

/**
 * @author Xavier F. Gouchet
 */
enum class CharConstraint {
    ANY,
    ASCII,
    ASCII_EXTENDED,
    HEXADECIMAL,
    ALPHA,
    ALPHA_NUM,
    NUMERICAL,
    WHITESPACE,
    NON_HEXADECIMAL,
    NON_ALPHA,
    NON_ALPHA_NUM,
    NON_NUMERICAL,
    NON_WHITESPACE,
}
