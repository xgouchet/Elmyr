package fr.xgouchet.elmyr.annotation

/**
 * Mark a field, property or method parameter as a Regex based String forgery.
 */
@Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FIELD,
        AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RegexForgery(
    /** The regex pattern to match. */
    val value: String
)
