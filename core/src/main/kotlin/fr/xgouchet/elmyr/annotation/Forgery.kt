package fr.xgouchet.elmyr.annotation

/**
 * Mark a field, property or method parameter as a forgery.
 *
 * Using some libraries allows one to inject a forged instance into the annotated
 * field/property/parameter, to simplify the writing of tests.
 *
 * TODO see the junit5, junit5 and … modules
 */
@Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FIELD,
        AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Forgery
