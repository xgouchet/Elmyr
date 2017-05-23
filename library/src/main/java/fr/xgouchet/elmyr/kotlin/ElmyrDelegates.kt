package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.*
import kotlin.properties.ReadOnlyProperty


/**
 * This object provide several delegates for forged properties
 * @author Xavier F. Gouchet
 */
object ElmyrDelegates {

    internal val FORGER: Forger = Forger()

    /**
     * Returns a property delegate for a read/write property with a non-`null` value that is initialized not during
     * object construction time but at a later time. Trying to read the property before the initial value has been
     * assigned results in an exception.
     */
    fun forgery(constraint: StringConstraint,
                case: Case = Case.ANY,
                size: Int = -1,
                forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, String>
            = ForgedString(constraint, case, size, forger = forger)

    /**
     * @param regex the regex to use to generate a String
     * @return a property delegate for a read-only property with a forged String based on the given Regex
     */
    fun forgery(
            regex: Regex,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, String>
            = ForgedString(regex = regex, forger = forger)

    /**
     * @param regex the regex to use to generate a String
     * @return a property delegate for a read-only property with a forged String based on the given Regex
     */
    fun forgery(
            regex: String,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, String>
            = ForgedString(regex = Regex(regex), forger = forger)

    /**
     * @param constraint the constraint for the Char to generate
     * @param case the case for the Char to generate
     * @return a property delegate for a read-only property with a forged Char based on the given constraints
     */
    fun forgery(
            constraint: CharConstraint,
            case: Case = Case.ANY,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, Char>
            = ForgedChar(constraint, case, forger = forger)

    /**
     * @param constraint the constraint for the Int to generate
     * @return a property delegate for a read-only property with a forged Int based on the given constraint
     */
    fun forgery(
            constraint: IntConstraint,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, Int>
            = ForgedInt(constraint, forger = forger)

    /**
     * @param min the min value (inclusive)
     * @param max the max value (exclusive)
     * @return a property delegate for a read-only property with a forged Int within the given range
     */
    fun forgery(
            min: Int,
            max: Int,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, Int>
            = ForgedInt(min = min, max = max, forger = forger)
}

