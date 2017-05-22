package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.*
import kotlin.properties.ReadOnlyProperty


/**
 * This object provide several delegates for forged properties
 * @author Xavier F. Gouchet
 */
object ElmyrDelegates {

    internal val forger: Forger = Forger()

    /**
     * Returns a property delegate for a read/write property with a non-`null` value that is initialized not during
     * object construction time but at a later time. Trying to read the property before the initial value has been
     * assigned results in an exception.
     */
    fun forgery(constraint: StringConstraint,
                case: Case = Case.ANY,
                size: Int = -1)
            : ReadOnlyProperty<Any?, String>
            = ForgedString(constraint, case, size)

    /**
     * @param regex the regex to use to generate a String
     * @return a property delegate for a read-only property with a forged String based on the given Regex
     */
    fun forgery(regex: Regex)
            : ReadOnlyProperty<Any?, String>
            = ForgedString(regex = regex)

    /**
     * @param regex the regex to use to generate a String
     * @return a property delegate for a read-only property with a forged String based on the given Regex
     */
    fun forgery(regex: String)
            : ReadOnlyProperty<Any?, String>
            = ForgedString(regex = Regex(regex))

    /**
     * @param constraint the constraint for the Char to generate
     * @param case the case for the Char to generate
     * @return a property delegate for a read-only property with a forged Char based on the given constraints
     */
    fun forgery(constraint: CharConstraint,
                case: Case = Case.ANY)
            : ReadOnlyProperty<Any?, Char>
            = ForgedChar(constraint, case)

    /**
     * @param constraint the constraint for the Int to generate
     * @return a property delegate for a read-only property with a forged Int based on the given constraint
     */
    fun forgery(constraint: IntConstraint)
            : ReadOnlyProperty<Any?, Int>
            = ForgedInt(constraint)

    /**
     * @param min the min value (inclusive)
     * @param max the max value (exclusive)
     * @return a property delegate for a read-only property with a forged Int within the given range
     */
    fun forgery(min: Int, max: Int)
            : ReadOnlyProperty<Any?, Int>
            = ForgedInt(min = min, max = max)
}

