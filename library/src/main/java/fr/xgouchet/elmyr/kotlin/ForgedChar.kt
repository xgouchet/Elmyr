package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Case
import fr.xgouchet.elmyr.CharConstraint
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A read-only Char property
 *
 * @author Xavier F. Gouchet
 */
class ForgedChar(
        constraint: CharConstraint = CharConstraint.ANY,
        case: Case = Case.ANY)
    : ReadOnlyProperty<Any?, Char> {

    internal val value: Char = ElmyrDelegates.forger.aChar(constraint, case)

    override fun getValue(thisRef: Any?, property: KProperty<*>): Char {
        return value
    }

}