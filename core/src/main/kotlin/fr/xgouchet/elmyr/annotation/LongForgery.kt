package fr.xgouchet.elmyr.annotation

/**
 * Mark a field, property or method parameter as a primitive long forgery.
 *
 * Using some libraries allows one to inject a forged instance into the annotated
 * field/property/parameter, to simplify the writing of tests.
 */
@Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FIELD,
        AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class LongForgery(
    val min: Long = Long.MIN_VALUE,
    val max: Long = Long.MAX_VALUE,
    val mean: Long = 0,
    val standardDeviation: Long = -1
)