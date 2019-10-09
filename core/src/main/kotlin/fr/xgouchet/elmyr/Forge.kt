package fr.xgouchet.elmyr

import fr.xgouchet.elmyr.kotlin.ForgedSequence
import java.util.Random
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sqrt

/**
 * The base class to generate forgeries.
 *
 * TODO include examples of all that's possible
 */
@Suppress("TooManyFunctions", "MethodOverloading")
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
     * Adds a factory to the forge. This is the best way to extend a forge and provides means to
     * create custom forgeries.
     * @param T the type the [ForgeryFactory] will be able to forge
     * @param forgeryFactory the factory to be used
     */
    inline fun <reified T : Any> addFactory(forgeryFactory: ForgeryFactory<T>) {
        addFactory(T::class.java, forgeryFactory)
    }

    /**
     * Adds a factory to the forge. This is the best way to extend a forge and provides means to
     * create custom forgeries.
     * @param T the type the [ForgeryFactory] will be able to forge
     * @param clazz the class of type T
     * @param forgeryFactory the factory to be used
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

        if (matches.isEmpty()) {
            throw ForgeryFactoryMissingException(clazz = clazz)
        }
        val factory = anElementFrom(matches.toList())
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
     * @throws [IllegalArgumentException] if the mean is outside of the range -46340..46340
     * (to avoid distribution imprecision); or if the standard deviation is negative
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

    // region Long

    /**
     * @param min the minimum value (inclusive), default = [Long.MIN_VALUE]
     * @param max the maximum value (exclusive), default = [Long.MAX_VALUE]
     * @return a long between min and max
     * @throws [IllegalArgumentException] if min >= max
     */
    @JvmOverloads
    fun aLong(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): Long {
        require(min < max) {
            "The ‘min’ boundary ($min) of the range should be less than the ‘max’ boundary ($max)"
        }

        val range = max - min
        return if (range < 0) {
            val halfRange = (max / 2) - (min / 2)
            ((abs(rng.nextLong()) % halfRange) * 2) + min
        } else {
            (abs(rng.nextLong()) % range) + min
        }
    }

    /**
     * @param strict if true, then it will return a non 0 long (default : false)
     * @return a positive long
     */
    @JvmOverloads
    fun aPositiveLong(strict: Boolean = false): Long {
        return aLong(min = if (strict) 1 else 0)
    }

    /**
     * @param strict if true, then it will return a non 0 long (default : true)
     * @return a negative long
     */
    @JvmOverloads
    fun aNegativeLong(strict: Boolean = true): Long {
        return aLong(min = Long.MIN_VALUE, max = if (strict) -1 else 0)
    }

    /**
     * @param mean the mean value of the distribution (default : 0)
     * @param standardDeviation the standard deviation value of the distribution (default : 100)
     * @return an long picked from a gaussian distribution (aka bell curve)
     */
    @JvmOverloads
    fun aGaussianLong(mean: Long = 0L, standardDeviation: Long = DEFAULT_STDEV_INT.toLong()): Long {
        require(mean <= MEAN_THRESHOLD_LONG) {
            "Cannot use a mean greater than $MEAN_THRESHOLD_LONG due to distribution imprecision"
        }
        require(mean >= -MEAN_THRESHOLD_LONG) {
            "Cannot use a mean less than -$MEAN_THRESHOLD_LONG due to distribution imprecision"
        }
        require(standardDeviation >= 0) {
            "Standard deviation ($standardDeviation) must be a positive (or null) value"
        }

        return if (standardDeviation == 0L) {
            mean
        } else {
            (rng.nextGaussian() * standardDeviation).roundToLong() + mean
        }
    }

    // endregion

    // region Float

    /**
     * @param min the minimum value (inclusive), default = -Float#MAX_VALUE
     * @param max the maximum value (exclusive), default = Float#MAX_VALUE
     * @return a float between min and max
     * @throws [IllegalArgumentException] if min > max
     */
    @JvmOverloads
    fun aFloat(min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE): Float {
        require(min <= max) {
            "The ‘min’ boundary ($min) of the range should be less than (or equal to) " +
                    "the ‘max’ boundary ($max)"
        }

        val range = max - min
        return if (range == Float.POSITIVE_INFINITY) {
            (rng.nextFloat() - HALF_PROBABILITY) * Float.MAX_VALUE * 2
        } else {
            (rng.nextFloat() * range) + min
        }
    }

    /**
     * @param strict if true, then it will return a non 0 int (default : false)
     * @return a positive float
     */
    @JvmOverloads
    fun aPositiveFloat(strict: Boolean = false): Float {
        return aFloat(min = if (strict) Float.MIN_VALUE else 0.0f)
    }

    /**
     * @param strict if true, then it will return a non 0 int (default : true)
     * @return a negative float
     */
    @JvmOverloads
    fun aNegativeFloat(strict: Boolean = true): Float {
        return -aPositiveFloat(strict)
    }

    /**
     * @param mean the mean value of the distribution (default : 0.0f)
     * @param standardDeviation the standard deviation value of the distribution (default : 1.0f)
     * @return a float picked from a gaussian distribution (aka bell curve)
     * @throws [IllegalArgumentException] if the mean is outside of the range -1.8446743E19..1.8446743E19
     * (to avoid distribution imprecision); or if the standard deviation is negative
     */
    @JvmOverloads
    fun aGaussianFloat(mean: Float = 0f, standardDeviation: Float = 1f): Float {

        require(mean <= MEAN_THRESHOLD_FLOAT) {
            "Cannot use a mean greater than $MEAN_THRESHOLD_FLOAT due to distribution imprecision"
        }
        require(mean >= -MEAN_THRESHOLD_FLOAT) {
            "Cannot use a mean less than -$MEAN_THRESHOLD_FLOAT due to distribution imprecision"
        }
        require(standardDeviation >= 0) {
            "Standard deviation ($standardDeviation) must be a positive (or null) value"
        }

        return if (standardDeviation == 0f) {
            mean
        } else {
            (rng.nextGaussian().toFloat() * standardDeviation) + mean
        }
    }

    // endregion

    // region Double

    /**
     * @param min the minimum value (inclusive), default = -Double#MAX_VALUE
     * @param max the maximum value (exclusive), default = Double#MAX_VALUE
     * @return a double between min and max
     * @throws [IllegalArgumentException] if min > max
     */
    @JvmOverloads
    fun aDouble(min: Double = -Double.MAX_VALUE, max: Double = Double.MAX_VALUE): Double {
        require(min <= max) {
            "The ‘min’ boundary ($min) of the range should be less than (or equal to) " +
                    "the ‘max’ boundary ($max)"
        }

        val range = max - min
        return if (range == Double.POSITIVE_INFINITY) {
            (rng.nextDouble() - HALF_PROBABILITY) * Double.MAX_VALUE * 2
        } else {
            (rng.nextDouble() * range) + min
        }
    }

    /**
     * @param strict if true, then it will return a non 0 int (default : false)
     * @return a positive double
     */
    @JvmOverloads
    fun aPositiveDouble(strict: Boolean = false): Double {
        return aDouble(min = if (strict) Double.MIN_VALUE else 0.0)
    }

    /**
     * @param strict if true, then it will return a non 0 int (default : true)
     * @return a negative double
     */
    @JvmOverloads
    fun aNegativeDouble(strict: Boolean = true): Double {
        return -aPositiveDouble(strict)
    }

    /**
     * @param mean the mean value of the distribution (default : 0.0)
     * @param standardDeviation the standard deviation value of the distribution (default : 1.0)
     * @return a double picked from a gaussian distribution (aka bell curve)
     * @throws [IllegalArgumentException] if the mean is outside of the range -1.8446743E19..1.8446743E19
     * (to avoid distribution imprecision); or if the standard deviation is negative
     */
    @JvmOverloads
    fun aGaussianDouble(mean: Double = 0.0, standardDeviation: Double = 1.0): Double {

        require(mean <= MEAN_THRESHOLD_DOUBLE) {
            "Cannot use a mean greater than $MEAN_THRESHOLD_DOUBLE due to distribution imprecision"
        }
        require(mean >= -MEAN_THRESHOLD_DOUBLE) {
            "Cannot use a mean less than -$MEAN_THRESHOLD_DOUBLE due to distribution imprecision"
        }
        require(standardDeviation >= 0) {
            "Standard deviation ($standardDeviation) must be a positive (or null) value"
        }

        return if (standardDeviation == 0.0) {
            mean
        } else {
            (rng.nextGaussian() * standardDeviation) + mean
        }
    }

    // endregion

    // region String

    /**
     * a String !
     */
    fun aString(): String {
        return "${anInt()}" // TODO
    }

    // endregion

    // region Element from …

    /**
     * @param set a non empty Set
     * @return an element “randomly” picked in the set
     */
    fun <T> anElementFrom(set: Set<T>): T {
        val index = anInt(0, set.size)
        return set.elementAt(index)
    }

    /**
     * @param list a non empty List
     * @return an element “randomly” picked in the list
     */
    fun <T> anElementFrom(list: List<T>): T {
        val index = anInt(0, list.size)
        return list[index]
    }

    /**
     * @param array a non empty Array
     * @return an element “randomly” picked in the array
     */
    fun <T> anElementFrom(vararg array: T): T {
        val index = anInt(0, array.size)
        return array[index]
    }

    /**
     * @param array a non empty BooleanArray
     * @return an element “randomly” picked in the array
     */
    fun anElementFrom(array: BooleanArray): Boolean {
        val index = anInt(0, array.size)
        return array[index]
    }

    /**
     * @param array a non empty CharArray
     * @return an element “randomly” picked in the array
     */
    fun anElementFrom(array: CharArray): Char {
        val index = anInt(0, array.size)
        return array[index]
    }

    /**
     * @param array a non empty IntArray
     * @return an element “randomly” picked in the array
     */
    fun anElementFrom(array: IntArray): Int {
        val index = anInt(0, array.size)
        return array[index]
    }

    /**
     * @param array a non empty LongArray
     * @return an element “randomly” picked in the array
     */
    fun anElementFrom(array: LongArray): Long {
        val index = anInt(0, array.size)
        return array[index]
    }

    /**
     * @param array a non empty FloatArray
     * @return an element “randomly” picked in the array
     */
    fun anElementFrom(array: FloatArray): Float {
        val index = anInt(0, array.size)
        return array[index]
    }

    /**
     * @param array a non empty DoubleArray
     * @return an element “randomly” picked in the array
     */
    fun anElementFrom(array: DoubleArray): Double {
        val index = anInt(0, array.size)
        return array[index]
    }

    /**
     * @param map a non empty Map
     * @return an element “randomly” picked in the set
     */
    fun <K, V> anEntryFrom(map: Map<K, V>): Map.Entry<K, V> {
        val index = anInt(0, map.size)
        return map.entries.elementAt(index)
    }

    /**
     * @param map a non empty map
     * @return a key randomly picked in the map
     */
    fun <K, V> aKeyFrom(map: Map<K, V>): K {
        return anEntryFrom(map).key
    }

    /**
     * @param map a non empty map
     * @return a key randomly picked in the map
     */
    fun <K, V> aValueFrom(map: Map<K, V>): V {
        return anEntryFrom(map).value
    }

    // endregion

    // region Collection manipulation

    /**
     * Creates a sub list of the given list, with random elements selected from the input. The order
     * of items in the result is not changed.
     *
     * @param list the list to choose from
     * @param outputSize the size of the sublist. If the input set is smaller than the given size,
     * the result will have the size of the input set. If set to -1 (default) a random size is picked.
     * @param T The type of elements in the list
     * @return a non null list, with elements picked at random in the input, without duplicates.
     * Note that if the input list contains duplicates, some might appear in the output.
     * The order in the output matches the input order
     */
    fun <T> aSubListOf(list: List<T>, outputSize: Int = -1): List<T> {
        val size = if (outputSize >= 0) outputSize else anInt(0, list.size)

        // fast exit : input too short
        if (list.size <= size) return ArrayList(list)

        // fast exit : output <= 0
        if (size <= 0) return emptyList()

        val inputSize = list.size
        val result = ArrayList<T>(size)
        val rng = Random()

        var numberOfItemsToChooseFrom: Int
        var numberOfItemsToSelect = size

        var i = 0
        while (i < inputSize && numberOfItemsToSelect > 0) {

            numberOfItemsToChooseFrom = inputSize - i
            val probabilityToSelectCurrent =
                    numberOfItemsToSelect.toDouble() / numberOfItemsToChooseFrom.toDouble()
            val randomProbability = rng.nextDouble()

            if (randomProbability < probabilityToSelectCurrent) {
                numberOfItemsToSelect--
                result.add(list[i])
            }
            i++
        }
        return result
    }

    /**
     * Creates a sub set of the given set, with random elements selected from the input.
     *
     * @param set the set to choose from
     * @param outputSize the size of the subset. If the input set is smaller than the given size,
     * the result will have the size of the input set. If set to -1 (default) a random size is picked.
     * @param T The type of elements in the set
     * @return a non null set, with elements picked at random in the input, without duplicates.
     */
    fun <T> aSubSetOf(set: Set<T>, outputSize: Int = -1): Set<T> {
        val size = if (outputSize >= 0) outputSize else anInt(0, set.size)

        // fast exit : input too short -> return all
        if (set.size <= size) return HashSet(set)

        // fast exit : output == 0
        if (size == 0) return emptySet()

        val setList = set.toList()
        val inputSize = set.size
        val result = HashSet<T>(size)
        val rng = Random()

        var numberOfItemsToChooseFrom: Int
        var numberOfItemsToSelect = size

        var i = 0
        while (i < inputSize && numberOfItemsToSelect > 0) {

            numberOfItemsToChooseFrom = inputSize - i
            val probabilityToSelectCurrent =
                    numberOfItemsToSelect.toDouble() / numberOfItemsToChooseFrom.toDouble()
            val randomProbability = rng.nextDouble()

            if (randomProbability < probabilityToSelectCurrent) {
                numberOfItemsToSelect--
                result.add(setList[i])
            }
            i++
        }
        return result
    }

    /**
     * Shuffles the order if the elements in a list (like shuffling a deck of card).
     * @param list the list to shuffle
     * @return a new list with the same elements as the input, but in a random order
     */
    fun <T> shuffle(list: List<T>): List<T> {
        // fast exit : input is empty
        if (list.isEmpty()) return emptyList()

        val result = Array<Any?>(list.size) { list[it] }

        for (i in 0..(list.size - 2)) {
            val j = anInt(i, list.size)
            val temp = result[i]
            result[i] = result[j]
            result[j] = temp
        }

        @Suppress("UNCHECKED_CAST")
        return result.asList() as List<T>
    }

    // endregion

    // region Collection generation

    /**
     * Creates a random list.
     * @param size the size of the list, or -1 for a random size
     * @param forging a lambda generating values that will fill the list
     * @param T The type of elements in the list
     */
    fun <T> aList(size: Int = -1, forging: Forge.() -> T): List<T> {
        val listSize = if (size < 0) aTinyInt() else size
        val list = ArrayList<T>(listSize)

        for (i in 0 until listSize) {
            list.add(forging(this))
        }

        return list
    }

    /**
     * Creates a random sequence.
     * @param size the size of the sequence, or -1 for a random size
     * @param forging a lambda generating values that will fill the list
     * @param T The type of elements in the list
     */
    fun <T> aSequence(size: Int = -1, forging: Forge.() -> T): Sequence<T> {
        val sequenceSize = if (size < 0) aTinyInt() else size
        return ForgedSequence(sequenceSize) { this@Forge.forging() }
    }

    /**
     * Returns a map with elements generated from the given lambda.
     *
     * Note that the resulting map size might be smaller than the requested one if the forging
     * lambda generates conflicting keys
     * @param size the size of the map, or -1 for a random size
     * @param forging a lambda generating a pair of key-value that will fill the map
     * @param K The type of keys in the map
     * @param V The type of values in the map
     */
    fun <K, V> aMap(size: Int = -1, forging: Forge.() -> Pair<K, V>): Map<K, V> {
        val mapSize = if (size < 0) aTinyInt() else size

        val map = mutableMapOf<K, V>()

        for (i in 0 until mapSize) {
            val mapEntry = forging()
            map[mapEntry.first] = mapEntry.second
        }

        return map
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

        // Gaussians
        @JvmField internal val MEAN_THRESHOLD_INT = sqrt(Int.MAX_VALUE.toDouble()).roundToInt()
        @JvmField internal val MEAN_THRESHOLD_LONG = sqrt(Long.MAX_VALUE.toDouble()).roundToLong()
        @JvmField internal val MEAN_THRESHOLD_FLOAT = sqrt(Float.MAX_VALUE.toDouble()).toFloat()
        @JvmField internal val MEAN_THRESHOLD_DOUBLE = sqrt(Double.MAX_VALUE)
    }
}
