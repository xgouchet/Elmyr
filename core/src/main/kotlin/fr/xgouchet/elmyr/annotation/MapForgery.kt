package fr.xgouchet.elmyr.annotation

import fr.xgouchet.elmyr.ForgeryFactory

/**
 * Mark a field, property or method parameter as a map forgery.
 *
 * @property key a [AdvancedForgery] describing how to forge the keys in the map.
 * Leave default if your Map uses custom objects with existing [ForgeryFactory] as keys.
 * @property value a [AdvancedForgery] describing how to forge the values in the map.
 * Leave default if your Map uses custom objects with existing [ForgeryFactory] as values.
 */
@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class MapForgery(
    val key: AdvancedForgery = AdvancedForgery(),
    val value: AdvancedForgery = AdvancedForgery()
)
