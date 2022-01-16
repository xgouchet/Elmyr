package fr.xgouchet.elmyr.annotation

/**
 * Mark a field, property or method parameter as a primitive double forgery.
 * You can only specify the `min` and `max` parameters, or the `mean` and `standardDeviation` parameters.
 *
 * @property min the minimum value (inclusive), default = -[Double.MAX_VALUE]
 * @property max the maximum value (exclusive), default = [Double.MAX_VALUE]
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
annotation class DoubleForgery(
    val min: Double = -java.lang.Double.MAX_VALUE,
    val max: Double = java.lang.Double.MAX_VALUE,
    val mean: Double = 0.0,
    val standardDeviation: Double = java.lang.Double.NaN
)
