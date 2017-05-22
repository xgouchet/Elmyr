package fr.xgouchet.elmyr

/**
 * @author Xavier F. Gouchet
 */
enum class CharConstraint {
    ANY,
    HEXADECIMAL,
    ALPHA,
    ALPHA_NUM,
    NUMERICAL,
    WHITESPACE,
    NOT_HEXADECIMAL,
    NOT_ALPHA,
    NOT_ALPHA_NUM,
    NOT_NUMERICAL,
    NOT_WHITESPACE,
}