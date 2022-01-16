package fr.xgouchet.elmyr.annotation

/**
 * Mark a field, property or method parameter as a primitive float forgery.
 * You can only specify the `min` and `max` parameters, or the `mean` and `standardDeviation` parameters.
 *
 * @property min the minimum value (inclusive), default = -[Float.MAX_VALUE]
 * @property max the maximum value (exclusive), default = [Float.MAX_VALUE]
 * @property mean the mean value of the distribution
 * @property standardDeviation the standard deviation value of the distribution
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
