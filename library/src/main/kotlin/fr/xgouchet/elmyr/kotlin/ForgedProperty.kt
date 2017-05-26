package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forger
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author Xavier F. Gouchet
 */
abstract class ForgedProperty<out T>(val forger: Forger)
    : ReadOnlyProperty<Any?, T> {

    val memoizedValue: T by lazy { -> generate(forger) }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return memoizedValue
    }

    abstract fun generate(forger: Forger): T
}