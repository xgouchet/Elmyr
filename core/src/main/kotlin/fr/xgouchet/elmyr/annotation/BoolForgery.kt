package fr.xgouchet.elmyr.annotation

import fr.xgouchet.elmyr.Forge

/**
 * Mark a field, property or method parameter as a primitive boolean forgery.
 * @property probability the probability the boolean will be true (default 0.5f)
 */
@Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FIELD,
        AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class BoolForgery(
    val probability: Float = Forge.HALF_PROBABILITY
)
