package fr.xgouchet.elmyr.annotation

/**
 * Mark a field, property or method parameter as a primitive long forgery.
 * You can only specify the `min` and `max` parameters, or the `mean` and `standardDeviation` parameters.
 *
 * @param min the minimum value (inclusive), default = [Long.MIN_VALUE]
 * @param max the maximum value (exclusive), default = [Long.MAX_VALUE]
 * @param mean the mean value of the distribution
 * @param standardDeviation the standard deviation value of the distribution
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
