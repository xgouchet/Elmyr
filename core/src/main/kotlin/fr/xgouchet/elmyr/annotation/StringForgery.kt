package fr.xgouchet.elmyr.annotation

import fr.xgouchet.elmyr.Case

/**
 * Mark a field, property or method parameter as a String forgery.
 *
 * @property type the [StringForgeryType] of String to forge ([StringForgeryType.ALPHABETICAL] by default)
 * @property case the case to use ([Case.ANY] by default, doesn't apply to regex). This will only be used for the
 * following types :
 * [StringForgeryType.ALPHABETICAL], [StringForgeryType.ALPHA_NUMERICAL], [StringForgeryType.NUMERICAL],
 * [StringForgeryType.HEXADECIMAL]
 * @property size the size of the String, or -1 for a random size (doesn't apply to regex).
 * @property regex the regex pattern to match (leave empty to use the type, case and size instead).
 */
@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class StringForgery(
    val type: StringForgeryType = StringForgeryType.ALPHABETICAL,
    val case: Case = Case.ANY,
    val size: Int = -1,
    val regex: String = ""
)
