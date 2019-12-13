package fr.xgouchet.elmyr.annotation

/**
 * Mark a field, property or method parameter as a Regex based String forgery.
 * @param value the regex pattern to match.
 */
@Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FIELD,
        AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RegexForgery(
    val value: String
)
