package fr.xgouchet.elmyr.annotation

/**
 * Mark a field, property or method parameter as a primitive integer forgery.
 * You can only specify the `min` and `max` parameters, or the `mean` and `standardDeviation` parameters.
 *
 * @param min the minimum value (inclusive), default = [Int.MIN_VALUE]
 * @param max the maximum value (exclusive), default = [Int.MAX_VALUE]
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
annotation class IntForgery(
    val min: Int = Int.MIN_VALUE,
    val max: Int = Int.MAX_VALUE,
    val mean: Int = 0,
    val standardDeviation: Int = -1
)
