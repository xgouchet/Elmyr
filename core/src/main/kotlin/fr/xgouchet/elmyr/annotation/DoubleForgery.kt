package fr.xgouchet.elmyr.annotation

/**
 * Mark a field, property or method parameter as a primitive double forgery.
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
annotation class DoubleForgery(
    val min: Double = -java.lang.Double.MAX_VALUE,
    val max: Double = java.lang.Double.MAX_VALUE,
    val mean: Double = 0.0,
    val standardDeviation: Double = java.lang.Double.NaN
)