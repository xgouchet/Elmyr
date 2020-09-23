package fr.xgouchet.elmyr.annotation

/**
 * Mark a field, property or method parameter as an advanced forgery.
 *
 * Advanced allow you to provide a list of ways the forgery can be created.
 * Only one of each parameter can be provided.
 *
 * @param string describes all the way a String can be forged
 * @param int describes all the way a Int can be forged
 * @param long describes all the way a Long can be forged
 * @param float describes all the way a Float can be forged
 * @param double describes all the way a Double can be forged
 * @param map describes all the way a Map can be forged
 * @param pair describes all the way a Pair can be forged
 */
@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class AdvancedForgery(
    val string: Array<StringForgery> = emptyArray(),
    val int: Array<IntForgery> = emptyArray(),
    val long: Array<LongForgery> = emptyArray(),
    val float: Array<FloatForgery> = emptyArray(),
    val double: Array<DoubleForgery> = emptyArray(),
    val map: Array<MapForgery> = emptyArray(),
    val pair: Array<PairForgery> = emptyArray()
)
