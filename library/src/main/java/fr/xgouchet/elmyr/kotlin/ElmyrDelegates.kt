package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Case
import fr.xgouchet.elmyr.Forger
import fr.xgouchet.elmyr.StringConstraint
import kotlin.properties.Delegates.notNull
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * @author Xavier F. Gouchet
 */
public object ElmyrDelegates {

    internal val forger: Forger = Forger()

    internal var any: String by notNull()

    /**
     * Returns a property delegate for a read/write property with a non-`null` value that is initialized not during
     * object construction time but at a later time. Trying to read the property before the initial value has been
     * assigned results in an exception.
     */
    public fun forgedString(constraint: StringConstraint = StringConstraint.ANY,
                            case: Case = Case.ANY,
                            size: Int = -1): ReadOnlyProperty<Any?, String> = ForgedString(constraint, case, size)

    /**
     * Returns a property delegate for a read/write property with a non-`null` value that is initialized not during
     * object construction time but at a later time. Trying to read the property before the initial value has been
     * assigned results in an exception.
     */
    public fun forgedString(regex: Regex): ReadOnlyProperty<Any?, String> = ForgedString(regex = regex)
}

private class ForgedString(
        constraint: StringConstraint = StringConstraint.ANY,
        case: Case = Case.ANY,
        size: Int = -1,
        regex: Regex? = null) : ReadOnlyProperty<Any?, String> {

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

private class ForgedChar(
        constraint: StringConstraint = StringConstraint.ANY,
        case: Case = Case.ANY,
        size: Int = -1,
        regex: Regex? = null) : ReadOnlyProperty<Any?, String> {

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