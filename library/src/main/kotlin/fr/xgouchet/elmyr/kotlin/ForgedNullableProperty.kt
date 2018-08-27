package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forger
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A forged property which could be null
 *
 * @author Xavier F. Gouchet
 */
class ForgedNullableProperty<T>(val delegate: ReadOnlyProperty<Any?, T>,
                                probability: Float = 0.5f,
                                val forger: Forger)
    : ReadOnlyProperty<Any?, T?> {

    val isNull: Boolean = forger.aBool(probability)

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return if (isNull) null else delegate.getValue(thisRef, property)
    }
}
