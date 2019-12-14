package fr.xgouchet.elmyr.annotation

import fr.xgouchet.elmyr.Case

/**
 * Mark a field, property or method parameter as a String forgery.
 *
 * @param value the [StringForgeryType] of String to forge
 * @param case the case to use ([Case.ANY] by default).
 * This will only be used for the following types :
 * [StringForgeryType.ALPHABETICAL], [StringForgeryType.ALPHA_NUMERICAL], [StringForgeryType.NUMERICAL],
 * [StringForgeryType.HEXADECIMAL]
 */
@Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FIELD,
        AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class StringForgery(
    val value: StringForgeryType,
    val case: Case = Case.ANY
)
