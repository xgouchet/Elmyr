package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Case
import fr.xgouchet.elmyr.StringConstraint
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A read-only String property
 *
 * @author Xavier F. Gouchet
 */
class ForgedString(
        constraint: StringConstraint = StringConstraint.ANY,
        case: Case = Case.ANY,
        size: Int = -1,
        regex: Regex? = null)
    : ReadOnlyProperty<Any?, String> {

    internal val value: String

    init {
        if (regex != null) {
            value = ElmyrDelegates.forger.aStringMatching(regex)
        } else {
            value = ElmyrDelegates.forger.aString(constraint, case, size)
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return value
    }

}