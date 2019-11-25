package fr.xgouchet.elmyr.annotation

/**
 * Mark a field, property or method parameter as a primitive float forgery.
 */
@Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FIELD,
        AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class FloatForgery(
    val min: Float = -java.lang.Float.MAX_VALUE,
    val max: Float = java.lang.Float.MAX_VALUE,
    val mean: Float = 0f,
    val standardDeviation: Float = java.lang.Float.NaN
)
