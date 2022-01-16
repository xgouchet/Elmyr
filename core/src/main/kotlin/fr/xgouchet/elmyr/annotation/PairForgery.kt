package fr.xgouchet.elmyr.annotation

import fr.xgouchet.elmyr.ForgeryFactory

/**
 * Mark a field, property or method parameter as a pair forgery.
 *
 * @property first a [AdvancedForgery] describing how to forge the first item in the pair.
 * Leave default if your Pair uses custom objects with existing [ForgeryFactory] as keys.
 * @property second a [AdvancedForgery] describing how to forge the second item in the pair.
 * Leave default if your Pair uses custom objects with existing [ForgeryFactory] as keys.
 */
@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class PairForgery(
    val first: AdvancedForgery = AdvancedForgery(),
    val second: AdvancedForgery = AdvancedForgery()
)
