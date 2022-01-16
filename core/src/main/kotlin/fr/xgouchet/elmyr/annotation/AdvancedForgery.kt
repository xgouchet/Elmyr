package fr.xgouchet.elmyr.annotation

/**
 * Mark a field, property or method parameter as an advanced forgery.
 *
 * Advanced allow you to provide a list of ways the forgery can be created.
 * Only one of each parameter can be provided.
 *
 * @property string describes all the way a String can be forged
 * @property int describes all the way a Int can be forged
 * @property long describes all the way a Long can be forged
 * @property float describes all the way a Float can be forged
 * @property double describes all the way a Double can be forged
 * @property map describes all the way a Map can be forged
 * @property pair describes all the way a Pair can be forged
 */
@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Suppress("LongParameterList")
annotation class AdvancedForgery(
    val string: Array<StringForgery> = [],
    val int: Array<IntForgery> = [],
    val long: Array<LongForgery> = [],
    val float: Array<FloatForgery> = [],
    val double: Array<DoubleForgery> = [],
    val map: Array<MapForgery> = [],
    val pair: Array<PairForgery> = []
)
