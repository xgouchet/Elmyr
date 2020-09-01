package fr.xgouchet.elmyr.annotation

/**
 * Mark a field, property or method parameter as a Regex based String forgery.
 * @param value the regex pattern to match.
 *
 * @deprecated Regex based string forgery can now be created using the StringForgery annotation
 * @see [StringForgery]
 */
@Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FIELD,
        AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
@Deprecated("Regex based string forgery can now be created using the StringForgery annotation")
@MustBeDocumented
annotation class RegexForgery(
    val value: String
)
