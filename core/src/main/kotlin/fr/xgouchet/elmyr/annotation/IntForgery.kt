package fr.xgouchet.elmyr.annotation

/**
 * Mark a field, property or method parameter as a primitive integer forgery.
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
annotation class IntForgery(
    val min: Int = Int.MIN_VALUE,
    val max: Int = Int.MAX_VALUE,
    val mean: Int = 0,
    val standardDeviation: Int = -1
)