package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Case
import fr.xgouchet.elmyr.CharConstraint
import fr.xgouchet.elmyr.IntConstraint
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A read-only Int property
 *
 * @author Xavier F. Gouchet
 */
class ForgedInt(
        constraint: IntConstraint = IntConstraint.ANY,
        min: Int = -1,
        max: Int = -1)
    : ReadOnlyProperty<Any?, Int> {

    internal val value: Int

    init {
        if ((min < 0) and (max < 0)) {
            value = ElmyrDelegates.forger.anInt(constraint)
        } else {
            value = ElmyrDelegates.forger.anInt(min, max)
        }
    }


    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return value
    }

}

