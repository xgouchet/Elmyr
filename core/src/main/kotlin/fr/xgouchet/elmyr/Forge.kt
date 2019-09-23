package fr.xgouchet.elmyr

import java.util.Random
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * The base class to generate forgeries.
 *
 * TODO include examples of all that's possible
 */
@Suppress("TooManyFunctions")
open class Forge {

    private val rng = Random()

    private val factories: MutableMap<Class<*>, ForgeryFactory<*>> = mutableMapOf()

    // region Reproducibility

    var seed: Long = System.nanoTime()
        set(value) {
            field = value
            rng.setSeed(seed)
        }

    // endregion

    // region Factory

    /**
     * Adds a factory to the forge. This is the best way to extend a forge and provides means to create custom forgeries.
     * @param T the type the [ForgeryFactory] will be able to forge
     */
    inline fun <reified T : Any> addFactory(forgeryFactory: ForgeryFactory<T>) {
        addFactory(T::class.java, forgeryFactory)
    }

    /**
     * Adds a factory to the forge. This is the best way to extend a forge and provides means to create custom forgeries.
     * @param T the type the [ForgeryFactory] will be able to forge
     * @param clazz the class of type T
     */
    fun <T : Any> addFactory(clazz: Class<T>, forgeryFactory: ForgeryFactory<T>) {
        factories[clazz] = forgeryFactory
    }

    /**
     * @param T the type of the instance to be forged
     * @return a new instance of type T, randomly forged with available factories
     * @throws [IllegalArgumentException] if no compatible factory exists
     */
    inline fun <reified T : Any> getForgery(): T {
        return getForgery(T::class.java)
    }

    /**
     * @param T the type of the instance to be forged
     * @param clazz the class of type T
     * @return a new instance of type T, randomly forged with available factories
     * @throws [IllegalArgumentException] if no compatible factory exists
     */
    fun <T : Any> getForgery(clazz: Class<T>): T {

        @Suppress("UNCHECKED_CAST")
        val strictMatch = factories[clazz] as? ForgeryFactory<T>

        @Suppress("IfThenToElvis")
        return if (strictMatch == null) {
            getSubclassForgery(clazz)
        } else {
            strictMatch.getForgery(this)
        }
    }

    private fun <T : Any> getSubclassForgery(clazz: Class<T>): T {

        val matches = factories.filterKeys {
            clazz.isAssignableFrom(it)
        }.values

        require(!matches.isEmpty()) {
            "Cannot create forgery for type ${clazz.canonicalName}.\n" +
                    "Make sure you provide a factory for this type."
        }
        val factory = if (aBool()) matches.first() else matches.last()
        @Suppress("UNCHECKED_CAST")
        return (factory as ForgeryFactory<T>).getForgery(this)
    }

    // endregion

    // region Bool

    /**
     * @param probability the probability the boolean will be true (default 0.5f)
     * @return a boolean
     */
    @JvmOverloads
    fun aBool(probability: Float = HALF_PROBABILITY): Boolean {
        return rng.nextFloat() < probability
    }

    // endregion

    // region Int

    /**
     * @param min the minimum value (inclusive), default = Int#MIN_VALUE
     * @param max the maximum value (exclusive), default = Int#MAX_VALUE
     * @return an int between min and max
     * @throws [IllegalArgumentException] if min >= max
     */
    @JvmOverloads
    fun anInt(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Int {
        require(min < max) {
            "The ‘min’ boundary ($min) of the range should be less than the ‘max’ boundary ($max)"
        }

        val range = max.toLong() - min.toLong()
        val rn = (abs(rng.nextLong()) % range) + min

        return (rn and MAX_INT).toInt()
    }

    /**
     * @param strict if true, then it will return a non 0 int (default : false)
     * @return a positive int
     */
    @JvmOverloads
    fun aPositiveInt(strict: Boolean = false): Int {
        return anInt(min = if (strict) 1 else 0)
    }

    /**
     * @param strict if true, then it will return a non 0 int (default : true)
     * @return a negative int
     */
    @JvmOverloads
    fun aNegativeInt(strict: Boolean = true): Int {
        return anInt(min = Int.MIN_VALUE, max = if (strict) -1 else 0)
    }

    /**
     * @return a strictly positive int, less than #TINY_THRESHOLD
     */
    fun aTinyInt(): Int {
        return anInt(1, TINY_THRESHOLD)
    }

    /**
     * @return a strictly positive int, less than  #SMALL_THRESHOLD
     */
    fun aSmallInt(): Int {
        return anInt(1, SMALL_THRESHOLD)
    }

    /**
     * @return a strictly positive int, greater than #BIG_THRESHOLD
     */
    fun aBigInt(): Int {
        return anInt(BIG_THRESHOLD)
    }

    /**
     * @return a strictly positive int, greater than #HUGE_THRESHOLD
     */
    fun aHugeInt(): Int {
        return anInt(HUGE_THRESHOLD)
    }

    /**
     * @param mean the mean value of the distribution (default : 0)
     * @param standardDeviation the standard deviation value of the distribution (default : 100)
     * @return an int picked from a gaussian distribution (aka bell curve)
     */
    @JvmOverloads
    fun aGaussianInt(mean: Int = 0, standardDeviation: Int = DEFAULT_STDEV_INT): Int {
        require(mean <= MEAN_THRESHOLD_INT) {
            "Cannot use a mean greater than $MEAN_THRESHOLD_INT due to distribution imprecision"
        }
        require(mean >= -MEAN_THRESHOLD_INT) {
            "Cannot use a mean less than -$MEAN_THRESHOLD_INT due to distribution imprecision"
        }
        require(standardDeviation >= 0) {
            "Standard deviation ($standardDeviation) must be a positive (or null) value"
        }

        return if (standardDeviation == 0) {
            mean
        } else {
            round((rng.nextGaussian() * standardDeviation)).toInt() + mean
        }
    }

    // endregion

    companion object {

        // Boolean
        internal const val HALF_PROBABILITY = 0.5f

        // Int
        internal const val TINY_THRESHOLD = 0x20
        internal const val SMALL_THRESHOLD = 0x100
        internal const val BIG_THRESHOLD = 0x10000
        internal const val HUGE_THRESHOLD = 0x1000000
        private const val MAX_INT = 0xFFFFFFFFL
        private const val DEFAULT_STDEV_INT = 100

        @JvmField internal val MEAN_THRESHOLD_INT = sqrt(Int.MAX_VALUE.toDouble()).roundToInt()
    }
}