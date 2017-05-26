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
    fun forgeryWithConstraint(constraint: StringConstraint,
                              case: Case = Case.ANY,
                              size: Int = -1,
                              forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, String>
            = ForgedString(constraint, case, size, forger = forger)

    /**
     * @param regex the regex to use to generate a String
     * @return a property delegate for a read-only property with a forged String based on the given Regex
     */
    fun forgeryWithRegex(
            regex: Regex,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, String>
            = ForgedString(regex = regex, forger = forger)

    /**
     * @param regex the regex to use to generate a String
     * @return a property delegate for a read-only property with a forged String based on the given Regex
     */
    fun forgeryWithRegex(
            regex: String,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, String>
            = ForgedString(regex = Regex(regex), forger = forger)

    /**
     * @param constraint the constraint for the Char to generate
     * @param case the case for the Char to generate
     * @return a property delegate for a read-only property with a forged Char based on the given constraints
     */
    fun forgeryWithConstraint(
            constraint: CharConstraint,
            case: Case = Case.ANY,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, Char>
            = ForgedChar(constraint, case, forger = forger)

    /**
     * @param constraint the constraint for the Int to generate
     * @return a property delegate for a read-only property with a forged Int based on the given constraint
     */
    fun forgeryWithConstraint(
            constraint: IntConstraint,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, Int>
            = ForgedInt(constraint, forger = forger)

    /**
     * @param min the min value (inclusive)
     * @param max the max value (exclusive)
     * @return a property delegate for a read-only property with a forged Int within the given range
     */
    fun forgeryWithRange(
            min: Int,
            max: Int,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, Int>
            = ForgedInt(min = min, max = max, forger = forger)

    /**
     * @param constraint the constraint for the Float to generate
     * @return a property delegate for a read-only property with a forged Int based on the given constraint
     */
    fun forgeryWithConstraint(
            constraint: FloatConstraint,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, Float>
            = ForgedFloat(constraint = constraint, forger = forger)

    /**
     * @param min the min value (inclusive)
     * @param max the max value (exclusive)
     * @return a property delegate for a read-only property with a forged Int within the given range
     */
    fun forgeryWithRange(
            min: Float = -Float.MAX_VALUE,
            max: Float = Float.MAX_VALUE,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, Float>
            = ForgedFloat(min = min, max = max, forger = forger)

    /**
     * @param mean the mean value of the distribution (default 0.0f)
     * @param standardDeviation the standard deviation value of the distribution (default 1.0f)
     * @return a property delegate for a read-only property with a forged Float in a gaussian distribution based on the
     * given mean and standard deviation
     */
    fun forgeryWithDistribution(
            mean: Float = 0f,
            standardDeviation: Float = 1f,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, Float>
            = ForgedFloat(mean = mean, standardDeviation = standardDeviation, forger = forger)


    /**
     * @param constraint the constraint for the Double to generate
     * @return a property delegate for a read-only property with a forged Int based on the given constraint
     */
    fun forgeryWithConstraint(
            constraint: DoubleConstraint,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, Double>
            = ForgedDouble(constraint = constraint, forger = forger)

    /**
     * @param min the min value (inclusive)
     * @param max the max value (exclusive)
     * @return a property delegate for a read-only property with a forged Int within the given range
     */
    fun forgeryWithRange(
            min: Double = -Double.MAX_VALUE,
            max: Double = Double.MAX_VALUE,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, Double>
            = ForgedDouble(min = min, max = max, forger = forger)

    /**
     * @param mean the mean value of the distribution (default 0.0f)
     * @param standardDeviation the standard deviation value of the distribution (default 1.0f)
     * @return a property delegate for a read-only property with a forged Double in a gaussian distribution based on the
     * given mean and standard deviation
     */
    fun forgeryWithDistribution(
            mean: Double = 0.0,
            standardDeviation: Double = 1.0,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, Double>
            = ForgedDouble(mean = mean, standardDeviation = standardDeviation, forger = forger)


    /**
     * Makes a delegate from another one, potentially returning null instead of the delegate value.
     *
     * Although this is to be used with other delegates from this class, it can work with any read-only delegate
     */
    fun <T> nullable(
            delegate: ReadOnlyProperty<Any?, T>,
            probability: Float = 0.5f,
            forger: Forger = FORGER)
            : ReadOnlyProperty<Any?, T?>
            = ForgedNullableProperty(delegate, probability, forger)
}

