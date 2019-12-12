package fr.xgouchet.elmyr.annotation

import fr.xgouchet.elmyr.Case

/**
 * Mark a field, property or method parameter as a String forgery.
 */
@Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FIELD,
        AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class StringForgery(
    /** the [Type] of String to forge. */
    val value: Type,
    /**
     * The case to use.
     *
     * This will only be used for the following types :
     * [Type.ALPHABETICAL], [Type.ALPHA_NUMERICAL], [Type.NUMERICAL], [Type.HEXADECIMAL]
     */
    val case: Case = Case.ANY
) {

    /**
     * The type of String to be forged.
     */
    enum class Type {
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
}
