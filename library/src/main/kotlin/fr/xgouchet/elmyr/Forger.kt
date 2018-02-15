package fr.xgouchet.elmyr

import fr.xgouchet.elmyr.regex.RegexBuilder
import org.junit.AssumptionViolatedException
import java.io.File
import java.lang.Integer.min
import java.lang.Math.round
import java.util.ArrayList
import java.util.Random
import java.util.concurrent.TimeUnit


/**
 * @author Xavier F. Gouchet
 */
open class Forger {

    private val rng = java.util.Random()
    var ignorePreconditionsErrors: Boolean = false

    var seed: Long

    init {
        seed = System.nanoTime()
        reset(seed)
    }

    /**
     * Resets this forger with the given seed. Knowing the seed allow the forger to reproduce
     * previous data.
     *
     * @param seed the seed to use (try and remember to be able to reproduce a forgery)
     */
    fun reset(seed: Long) {
        this.seed = seed
        rng.setSeed(seed)
    }

    // region Bool

    /**
     * @param probability the probability the boolean will be true (default 0.5f)
     * @return a boolean
     */
    @JvmOverloads
    fun aBool(probability: Float = 0.5f): Boolean {
        return rng.nextFloat() < probability
    }

    // endregion

    // region Int

    /**
     * @param constraint a constraint on the int to forge
     * @return an int between constraint and max
     */
    fun anInt(constraint: IntConstraint): Int {
        when (constraint) {
            IntConstraint.ANY -> return anInt()
            IntConstraint.TINY -> return aTinyInt()
            IntConstraint.SMALL -> return aSmallInt()
            IntConstraint.BIG -> return aBigInt()
            IntConstraint.HUGE -> return aHugeInt()
            IntConstraint.POSITIVE -> return aPositiveInt(strict = false)
            IntConstraint.POSITIVE_STRICT -> return aPositiveInt(strict = true)
            IntConstraint.NEGATIVE -> return aNegativeInt(strict = false)
            IntConstraint.NEGATIVE_STRICT -> return aNegativeInt(strict = true)

            else -> preconditionException("Unexpected constraint : $constraint")
        }
    }

    /**
     * @param min the minimum value (inclusive), default = Int#MIN_VALUE
     * @param max the maximum value (exclusive), default = Int#MAX_VALUE
     * @return an int between min and max
     */
    @JvmOverloads
    fun anInt(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Int {

        if (min >= max) {
            preconditionException("The ‘min’ boundary ($min) of the range should be less than the ‘max’ boundary ($max)")
        }

        val range = max.toLong() - min.toLong()
        val rn = (Math.abs(rng.nextLong()) % range) + min

        return (rn and 0xFFFFFFFFL).toInt()
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
    fun aGaussianInt(mean: Int = 0, standardDeviation: Int = 100): Int {
        if (mean > MEAN_THRESHOLD_INT) {
            preconditionException("Cannot use a mean greater than $MEAN_THRESHOLD_INT due to distribution imprecision")
        }
        if (mean < -MEAN_THRESHOLD_INT) {
            preconditionException("Cannot use a mean less than -$MEAN_THRESHOLD_INT due to distribution imprecision")
        }
        if (standardDeviation < 0) {
            preconditionException("Standard deviation ($standardDeviation) must be a positive (or null) value")
        }

        if (standardDeviation == 0) {
            return mean
        } else {
            return round((rng.nextGaussian() * standardDeviation)).toInt() + mean
        }
    }

    // endregion

    // region Long

    /**
     * @param constraint a constraint on the long to forge
     * @return an long between constraint and max
     */
    fun aLong(constraint: LongConstraint): Long {
        when (constraint) {
            LongConstraint.ANY -> return aLong()
            LongConstraint.POSITIVE -> return aPositiveLong()
            LongConstraint.POSITIVE_STRICT -> return aPositiveLong(strict = true)
            LongConstraint.NEGATIVE -> return aNegativeLong()
            LongConstraint.NEGATIVE_STRICT -> return aNegativeLong(strict = true)

            else -> preconditionException("Unexpected constraint : $constraint")
        }
    }

    /**
     * @param min the minimum value (inclusive), default = Long#MIN_VALUE
     * @param max the maximum value (exclusive), default = Long#MAX_VALUE
     * @return an long between min and max
     */
    @JvmOverloads
    fun aLong(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): Long {

        if (min >= max) {
            preconditionException("The ‘min’ boundary ($min) of the range should be less than the ‘max’ boundary ($max)")
        }

        val range = max - min

        return (Math.abs(rng.nextLong()) % range) + min
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
    fun aGaussianLong(mean: Long = 0L, standardDeviation: Long = 100L): Long {
        if (mean > MEAN_THRESHOLD_LONG) {
            preconditionException("Cannot use a mean greater than $MEAN_THRESHOLD_LONG due to distribution imprecision")
        }
        if (mean < -MEAN_THRESHOLD_LONG) {
            preconditionException("Cannot use a mean less than -$MEAN_THRESHOLD_LONG due to distribution imprecision")
        }
        if (standardDeviation < 0L) {
            preconditionException("Standard deviation ($standardDeviation) must be a positive (or null) value")
        }

        if (standardDeviation == 0L) {
            return mean
        } else {
            return round((rng.nextGaussian() * standardDeviation)) + mean
        }
    }

    /**
     * @return a long to be used as a timestamp, picked in a milliseconds range around today
     */
    fun aTimestamp(range: Long = ONE_YEAR, unit: TimeUnit = TimeUnit.MILLISECONDS): Long {
        if (range <= 0) {
            preconditionException("Time range ($range ms) must be strictly positive")
        }

        val rangeMs = unit.toMillis(range)
        val min = -rangeMs
        val now = System.currentTimeMillis()
        return now + aLong(min, rangeMs)
    }

    // endregion

    // region Float

    /**
     * @param constraint a constraint on the float to forge
     * @return a float between constraint and max
     */
    fun aFloat(constraint: FloatConstraint): Float {
        when (constraint) {
            FloatConstraint.ANY -> return aFloat()
            FloatConstraint.POSITIVE -> return aPositiveFloat()
            FloatConstraint.POSITIVE_STRICT -> return aPositiveFloat(strict = true)
            FloatConstraint.NEGATIVE -> return aNegativeFloat()
            FloatConstraint.NEGATIVE_STRICT -> return aNegativeFloat(strict = true)

            else -> preconditionException("Unexpected constraint : $constraint")
        }
    }

    /**
     * @param min the minimum value (inclusive), default = -Float#MAX_VALUE
     * @param max the maximum value (exclusive), default = Float#MAX_VALUE
     * @return a float between min and max
     */
    @JvmOverloads
    fun aFloat(min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE): Float {

        if (min > max) {
            preconditionException("The ‘min’ boundary ($min) of the range should be less than (or equal to) the ‘max’ boundary ($max)")
        }

        val range = max - min
        if (range == Float.POSITIVE_INFINITY) {
            return (rng.nextFloat() - 0.5f) * Float.MAX_VALUE * 2
        } else {
            return (rng.nextFloat() * range) + min
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
     */
    @JvmOverloads
    fun aGaussianFloat(mean: Float = 0f, standardDeviation: Float = 1f): Float {
        if (mean > MEAN_THRESHOLD_FLOAT) {
            preconditionException("Cannot use a mean greater than $MEAN_THRESHOLD_FLOAT due to floating point precision error")
        }
        if (mean < -MEAN_THRESHOLD_FLOAT) {
            preconditionException("Cannot use a mean less than -$MEAN_THRESHOLD_FLOAT due to floating point precision error")
        }
        if (standardDeviation < 0) {
            preconditionException("Standard deviation ($standardDeviation) must be a positive (or null) value")
        }

        if (standardDeviation == 0f) {
            return mean
        } else {
            return (rng.nextGaussian().toFloat() * standardDeviation) + mean
        }
    }

    // endregion

    // region Double

    /**
     * @param constraint a constraint on the double to forge
     * @return a double between constraint and max
     */
    fun aDouble(constraint: DoubleConstraint): Double {
        when (constraint) {
            DoubleConstraint.ANY -> return aDouble()
            DoubleConstraint.POSITIVE -> return aPositiveDouble(strict = false)
            DoubleConstraint.POSITIVE_STRICT -> return aPositiveDouble(strict = true)
            DoubleConstraint.NEGATIVE -> return aNegativeDouble(strict = false)
            DoubleConstraint.NEGATIVE_STRICT -> return aNegativeDouble(strict = true)

            else -> preconditionException("Unexpected constraint : $constraint")
        }
    }

    /**
     * @param min the minimum value (inclusive), default = -Double#MAX_VALUE
     * @param max the maximum value (exclusive), default = Double#MAX_VALUE
     * @return a double between min and max
     */
    @JvmOverloads
    fun aDouble(min: Double = -Double.MAX_VALUE, max: Double = Double.MAX_VALUE): Double {

        if (min > max) {
            preconditionException("The ‘min’ boundary ($min) of the range should be less than (or equal to) the ‘max’ boundary ($max)")
        }

        val range = max - min
        if (range == Double.POSITIVE_INFINITY) {
            return (rng.nextDouble() - 0.5f) * Double.MAX_VALUE * 2
        } else {
            return (rng.nextDouble() * range) + min
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
     * @param mean the mean value of the distribution (default : 0.0f)
     * @param standardDeviation the standard deviation value of the distribution (default : 1.0f)
     * @return a double picked from a gaussian distribution (aka bell curve)
     */
    @JvmOverloads
    fun aGaussianDouble(mean: Double = 0.0, standardDeviation: Double = 1.0): Double {
        if (mean > MEAN_THRESHOLD_DOUBLE) {
            preconditionException("Cannot use a mean greater than $MEAN_THRESHOLD_DOUBLE due to floating point precision error")
        }
        if (mean < -MEAN_THRESHOLD_DOUBLE) {
            preconditionException("Cannot use a mean less than -$MEAN_THRESHOLD_DOUBLE due to floating point precision error")
        }
        if (standardDeviation < 0) {
            preconditionException("Standard deviation ($standardDeviation) must be a positive (or null) value")
        }

        if (standardDeviation == 0.0) {
            return mean
        } else {
            return (rng.nextGaussian() * standardDeviation) + mean
        }
    }

    // endregion

    // region Char

    /**
     * @param constraint a constraint on the char to forge
     * @param case the case to use (depending on the constraint, it might be ignored)
     * @param forbiddenChars an array of characters forbidden within the constraint
     * @return a Char with the given constraints
     */
    @JvmOverloads
    fun aChar(constraint: CharConstraint,
              case: Case = Case.ANY,
              forbiddenChars: CharArray? = null): Char {
        var result: Char

        do {
            when (constraint) {
                CharConstraint.ANY -> result = aChar()
                CharConstraint.HEXADECIMAL -> result = anHexadecimalChar(case)
                CharConstraint.ASCII -> result = anAsciiChar()
                CharConstraint.ASCII_EXTENDED -> result = anExtendedAsciiChar()
                CharConstraint.ALPHA -> result = anAlphabeticalChar(case)
                CharConstraint.ALPHA_NUM -> result = anAlphaNumericalChar(case)
                CharConstraint.NUMERICAL -> result = aNumericalChar()
                CharConstraint.WHITESPACE -> result = aWhitespaceChar()
                CharConstraint.NON_HEXADECIMAL -> result = aNonHexadecimalChar()
                CharConstraint.NON_ALPHA -> result = aNonAlphabeticalChar()
                CharConstraint.NON_ALPHA_NUM -> result = aNonAlphaNumericalChar()
                CharConstraint.NON_NUMERICAL -> result = aNonNumericalChar()
                CharConstraint.NON_WHITESPACE -> result = aNonWhitespaceChar()
            }
        } while (forbiddenChars != null && result in forbiddenChars)

        return result
    }

    /**
     * @param min the min char code to use (inclusive, default = 0x20 == space)
     * @param max the max char code to use (exclusive, default = 0xD800)
     * @return a Char within the given range
     */
    @JvmOverloads
    fun aChar(min: Char = MIN_PRINTABLE, max: Char = MAX_UTF8): Char {
        return anInt(min.toInt(), max.toInt()).toChar()
    }

    /**
     * @return a Char within the standard ASCII printable characters
     */
    fun anAsciiChar(): Char {
        return aChar(MIN_PRINTABLE, MAX_ASCII)
    }

    /**
     * @return a Char within the extended ASCII printable characters
     */
    fun anExtendedAsciiChar(): Char {
        return aChar(MIN_PRINTABLE, MAX_ASCII_EXTENDED)
    }

    /**
     * @param case the case to use (supports Case.UPPER, Case.LOWER and Case.ANY, anything else falls back to Case.ANY)
     * @return an alpha character (from the roman alphabet), in the given case
     */
    @JvmOverloads
    fun anAlphabeticalChar(case: Case = Case.ANY): Char {
        when (case) {
            Case.UPPER -> return anElementFrom(ALPHA_UPPER)
            Case.LOWER -> return anElementFrom(ALPHA_LOWER)
            else -> return anElementFrom(ALPHA)
        }
    }

    /**
     * @return a character which is not alphabetical
     */
    fun aNonAlphabeticalChar(): Char {
        var res: Char
        do {
            res = aChar(CharConstraint.ANY, Case.ANY)
        } while (ALPHA.contains(res))
        return res
    }

    /**
     * @param case the case to use (supports Case.UPPER, Case.LOWER and Case.ANY, anything else falls back to Case.ANY)
     * @return a standard vowel character (‘a’, ‘e’, ‘i’, ‘o’, ‘u’, ‘y’), in the given case
     */
    @JvmOverloads
    fun aVowelChar(case: Case = Case.ANY): Char {
        when (case) {
            Case.UPPER -> return anElementFrom(VOWEL_UPPER)
            Case.LOWER -> return anElementFrom(VOWEL_LOWER)
            else -> return anElementFrom(VOWEL)
        }
    }

    /**
     * @param case the case to use (supports Case.UPPER, Case.LOWER and Case.ANY, anything else falls back to Case.ANY)
     * @return a standard consonant character (any roman alphabet except ‘a’, ‘e’, ‘i’, ‘o’, ‘u’, ‘y’), in the given case
     */
    @JvmOverloads
    fun aConsonantChar(case: Case = Case.ANY): Char {
        when (case) {
            Case.UPPER -> return anElementFrom(CONSONANT_UPPER)
            Case.LOWER -> return anElementFrom(CONSONANT_LOWER)
            else -> return anElementFrom(CONSONANT)
        }
    }

    /**
     * @param case the case to use (supports Case.UPPER, Case.LOWER and Case.ANY, anything else falls back to Case.ANY)
     * @return an alphabetical or digit character, in the given case
     */
    @JvmOverloads
    fun anAlphaNumericalChar(case: Case = Case.ANY): Char {
        when (case) {
            Case.UPPER -> return anElementFrom(ALPHA_NUM_UPPER)
            Case.LOWER -> return anElementFrom(ALPHA_NUM_LOWER)
            else -> return anElementFrom(ALPHA_NUM)
        }
    }

    /**
     * @return a character neither alphabetical nor numeric
     */
    fun aNonAlphaNumericalChar(): Char {
        var res: Char
        do {
            res = aChar(CharConstraint.ANY, Case.ANY)
        } while (ALPHA_NUM.contains(res))
        return res
    }

    /**
     * @param case the case to use (supports Case.UPPER, Case.LOWER , anything else falls back to Case.LOWER)
     * @return a digit (0 to F)
     */
    @JvmOverloads
    fun anHexadecimalChar(case: Case = Case.LOWER): Char {
        when (case) {
            Case.UPPER -> return anElementFrom(HEXA_UPPER)
            else -> return anElementFrom(HEXA_LOWER)
        }
    }

    /**
     * @return a character that is not an hexadecimal digit
     */
    fun aNonHexadecimalChar(): Char {
        var res: Char
        do {
            res = aChar(CharConstraint.ANY, Case.ANY)
        } while (HEXA_LOWER.contains(res) or HEXA_UPPER.contains(res))
        return res
    }

    /**
     * @return a numerical (0 to 9)
     */
    fun aNumericalChar(): Char {
        return anElementFrom(DIGIT)
    }

    /**
     * a non numerical character
     */
    fun aNonNumericalChar(): Char {
        var res: Char
        do {
            res = aChar(CharConstraint.ANY, Case.ANY)
        } while (DIGIT.contains(res))
        return res
    }

    /**
     * @return a whitespace character
     */
    fun aWhitespaceChar(): Char {
        return anElementFrom(WHITESPACE)
    }

    /**
     * @return a non whitespace characer
     */
    fun aNonWhitespaceChar(): Char {
        var res: Char
        do {
            res = aChar(CharConstraint.ANY, Case.ANY)
        } while (WHITESPACE.contains(res))
        return res
    }
    // endregion

    // region String

    /**
     * @param constraint the constraint to use (default : ANY)
     * @param case the case to use (ignored when constraint is Any)
     * @param size the size of the string (or -1 for a random sized String)
     * @return a random string following the given constraint
     */
    @JvmOverloads
    fun aString(constraint: StringConstraint = StringConstraint.ANY,
                case: Case = Case.ANY,
                size: Int = -1): String {
        when (constraint) {
            StringConstraint.ANY -> return String(CharArray(getWordSize(size), { aChar(CharConstraint.ANY, Case.ANY) }))
            StringConstraint.WORD -> return aWord(case, size)
            StringConstraint.LIPSUM -> return aSentence(case, size)
            StringConstraint.HEXADECIMAL -> return anHexadecimalString(case, size)
            StringConstraint.URL -> return aUrl()
            StringConstraint.URI -> return aUri()
            StringConstraint.EMAIL -> return anEmail()
            StringConstraint.PATH -> return aLocalPath()
            StringConstraint.PATH_LINUX -> return aLinuxPath()
            StringConstraint.PATH_WINDOWS -> return aWindowsPath()
            StringConstraint.PATH_MACOS -> return aMacOsPath()

            else -> preconditionException("Unexpected constraint : $constraint")
        }
    }

    /**
     * @param constraint the character constraint to use (default : ANY)
     * @param case the case to use (ignored when constraint is Any)
     * @param size the size of the string (or -1 for a random sized String)
     * @param forbiddenChars an array of forbidden characters (or null if all characters (within the constraints) are allowed)
     * @return a random string whose chars follow the given constraint
     */
    @JvmOverloads
    fun aString(constraint: CharConstraint,
                case: Case = Case.ANY,
                size: Int = -1,
                forbiddenChars: CharArray? = null): String {
        return String(CharArray(getWordSize(size), { aChar(constraint, case, forbiddenChars) }))
    }

    /**
     * @param case the case to use (supports Case.UPPER, Case.LOWER, Case.CAPITALIZE or Case.ANY)
     * @param size the size of the string (or -1 for a random sized String)
     * @return a String that kind of look like a word
     */
    @JvmOverloads
    fun aWord(case: Case = Case.ANY, size: Int = -1): String {
        var consonant: Boolean = aBool()
        val resultSize = getWordSize(size)

        val array = CharArray(resultSize)
        var currentCase = case
        for (i in 0 until resultSize) {
            if (case == Case.CAPITALIZE) {
                currentCase = if (i == 0) Case.UPPER else Case.LOWER
            }
            array[i] = if (consonant) aConsonantChar(currentCase) else aVowelChar(currentCase)
            consonant = !consonant
        }

        return String(array)
    }

    /**
     * @param case the case to use (supports Case.UPPER, Case.LOWER, Case.CAPITALIZE, Case.CAPITALIZED_SENTENCE or Case.ANY)
     * @param size the size of the string (or -1 for a random sized String). Note that to construct a good sentence, the
     * size should be at least 3 characters long
     *
     * @return a String that kind of look like a sentence (think Lorem Ipsum)
     */
    @JvmOverloads
    fun aSentence(case: Case = Case.ANY, size: Int = -1): String {
        val resultSize: Int = if (size > 0) size else (aSmallInt() + 4)

        // The only way to have a punctuated sentende. Kind of
        if (resultSize == 1) return "‽"

        val builder = StringBuilder()

        while (builder.length < resultSize) {
            val actualCase: Case
            if (case == Case.CAPITALIZED_SENTENCE) {
                actualCase = if (builder.isEmpty()) Case.CAPITALIZE else Case.LOWER
            } else {
                actualCase = case
            }
            val remainingSize = resultSize - builder.length

            if (remainingSize < 7) {
                builder.append(aWord(actualCase, remainingSize - 1))
                builder.append(".") // TODO maybe randomize the punctuation ?
            } else {
                val wordSize = min(anInt(2, 10), remainingSize - 5)
                builder.append(aWord(actualCase, wordSize))
                builder.append(" ")
            }
        }

        return builder.toString()
    }

    /**
     * @param case the case to use (supports Case.UPPER, Case.LOWER , anything else falls back to Case.LOWER)
     * @param size the size of the string (or -1 for a random sized String)
     * @return an hexadecimal string
     */
    @JvmOverloads
    fun anHexadecimalString(case: Case = Case.LOWER, size: Int = -1): String {
        val resultSize = getWordSize(size)
        return String(CharArray(resultSize, { anHexadecimalChar(case) }))
    }

    /**
     * @param regex a regular expression to drive the generation. Note that parsing the regex can take some time depending
     * on the regex complexity. Also not all regex feature are supported.
     *
     * @return a String matching the given regular expression
     */
    fun aStringMatching(regex: String): String {
        return RegexBuilder(regex).buildString(this)
    }

    /**
     * @param regex a regular expression to drive the generation. Note that parsing the regex can take some time depending
     * on the regex complexity. Also not all regex feature are supported.
     *
     * @return a String matching the given regular expression
     */
    fun aStringMatching(regex: Regex): String {
        return aStringMatching(regex.pattern)
    }

    /**
     * @param absolute whether to build an absolute or relative path
     * @return a String matching a standard path for the current OS
     */
    @JvmOverloads
    fun aLocalPath(absolute: Boolean? = null): String {
        val osName = System.getProperty("os.name")
        if (osName.contains("win")) {
            return aWindowsPath(absolute)
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return aLinuxPath(absolute)
        } else if (osName.contains("mac")) {
            return aMacOsPath(absolute)
        } else {
            unsupportedFeature("Unsupported OS path format for “$osName”")
        }
    }

    /**
     * @param absolute whether to build an absolute or relative path
     * @return a String matching a standard Linux path format
     */
    @JvmOverloads
    fun aLinuxPath(absolute: Boolean? = null): String {
        val isAbsolute = absolute ?: aBool()
        val ancestorRoot = Array(aTinyInt(), { ".." }).joinToString(UNIX_SEP.toString()) { it }
        val roots = if (isAbsolute) LINUX_ROOTS else listOf(".", ancestorRoot)
        val forbiddenChars = arrayOf(0.toChar(), UNIX_SEP).toCharArray()
        return aPath(UNIX_SEP.toString(), roots, 1024, 128, forbiddenChars)
    }

    /**
     * @param absolute whether to build an absolute or relative path
     * @return a String matching a standard MacOs path format
     */
    @JvmOverloads
    fun aWindowsPath(absolute: Boolean? = null): String {
        val isAbsolute = absolute ?: aBool()
        val ancestorRoot = Array(aTinyInt(), { ".." }).joinToString(WINDOWS_SEP.toString()) { it }
        val roots = if (isAbsolute) WINDOWS_ROOTS else listOf(".", ancestorRoot)
        return aPath(WINDOWS_SEP.toString(), roots, 1024, 128, WINDOWS_FORBIDDEN_CHARS, WINDOWS_RESERVED_FILENAMES)
    }

    /**
     * @param absolute whether to build an absolute or relative path
     * @return a String matching a standard MacOs path format
     */
    @JvmOverloads
    fun aMacOsPath(absolute: Boolean? = null): String {
        val isAbsolute = absolute ?: aBool()
        val ancestorRoot = Array(aTinyInt(), { ".." }).joinToString(UNIX_SEP.toString()) { it }
        val roots = if (isAbsolute) MAC_ROOTS else listOf(".", ancestorRoot)
        val forbiddenChars = arrayOf(0.toChar(), UNIX_SEP).toCharArray()
        return aPath(UNIX_SEP.toString(), roots, 1024, 128, forbiddenChars)
    }

    /**
     * @return a String matching a standard URI according to RFC 3986
     */
    fun aUri(): String {
        val builder = StringBuilder()
        RFCDefinitions.RFC3986_buildURI(this, builder)
        return builder.toString()
    }

    /**
     * @return a String matching a standard URL according to RFC 3986
     * (Although the RFC itself doesn't give a clear definition of which URI are proper URL, we use a relatively broad definition)
     */
    fun aUrl(): String {
        val builder = StringBuilder()
        RFCDefinitions.RFC3986_buildURL(this, builder)
        return builder.toString()
    }

    /**
     * @param rfc2822Compliant if true, it will return an email compliant with the RFC2822 broader format
     * @return an email String
     */
    fun anEmail(rfc2822Compliant: Boolean = false): String {
        val builder = StringBuilder(255)
        if (rfc2822Compliant) {
            RFCDefinitions.RFC2822_buildEmail(this, builder)
        } else {
            RFCDefinitions.RFC822_buildEmail(this, builder)
        }
        return builder.toString()
    }

    /**
     * @return an IP address (using the IPv4 format)
     */
    fun anIPv4Address(): String {
        val builder = StringBuilder(16)
        RFCDefinitions.RFC791_buildIPv4Address(this, builder)
        return builder.toString()
    }

    /**
     * @return an IP address (using the IPv6 format)
     */
    fun anIPv6Address(): String {
        val builder = StringBuilder(48)
        RFCDefinitions.RFC4291_buildIPv6Address(this, builder)
        return builder.toString()
    }

    // endregion

    // region Nullable

    /**
     * @param value the value to use if not null
     * @param probability the probability the result will be null (default 0.5f)
     * @return either the given value, or null (with the given probability)
     */
    @JvmOverloads
    fun <T> aNullableFrom(value: T, nullProbability: Float = 0.5f): T? {
        if (aBool(nullProbability)) {
            return null
        } else {
            return value
        }
    }

    // endregion

    // region Collections

    /**
     * @param map a non empty Map
     * @return an element “randomly” picked in the set
     */
    fun <K, V> anEntryFrom(map: Map<K, V>): Map.Entry<K, V> {
        val index = anInt(0, map.size)
        return map.entries.elementAt(index)
    }

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
     * @return an element “randomly” picked in the
     */
    fun anElementFrom(array: DoubleArray): Double {
        val index = anInt(0, array.size)
        return array[index]
    }

    /**
     * @param constraint a constraint on the ints to forge
     * @param size the size of the array, or -1 for a random size
     * @return an array of int
     */
    @JvmOverloads
    fun anIntArray(constraint: IntConstraint, size: Int = -1): IntArray {
        val arraySize = if (size < 0) aTinyInt() else size
        return IntArray(arraySize, { anInt(constraint) })
    }

    /**
     * @param min the minimum value for all ints in the array
     * @param max the maximum value for all ints in the array
     * @param size the size of the array, or -1 for a random size
     * @return an array of int
     */
    @JvmOverloads
    fun anIntArray(min: Int, max: Int, size: Int = -1): IntArray {
        val arraySize = if (size < 0) aTinyInt() else size
        return IntArray(arraySize, { anInt(min, max) })
    }

    /**
     * @param mean the mean value of the distribution (default : 0)
     * @param standardDeviation the standard deviation value of the distribution (default : 100)
     * @param size the size of the array, or -1 for a random size
     * @return an array of int with a gaussian distribution
     */
    @JvmOverloads
    fun anIntArrayWithDistribution(mean: Int = 0, standardDeviation: Int = 100, size: Int = -1): IntArray {
        val arraySize = if (size < 0) aTinyInt() else size
        return IntArray(arraySize, { aGaussianInt(mean, standardDeviation) })
    }

    /**
     * @param constraint a constraint on the ints to forge
     * @param size the size of the array, or -1 for a random size
     * @return an array of long
     */
    @JvmOverloads
    fun aLongArray(constraint: LongConstraint, size: Int = -1): LongArray {
        val arraySize = if (size < 0) aTinyInt() else size
        return LongArray(arraySize, { aLong(constraint) })
    }

    /**
     * @param min the minimum value for all longs in the array
     * @param max the maximum value for all longs in the array
     * @param size the size of the array, or -1 for a random size
     * @return an array of long
     */
    @JvmOverloads
    fun aLongArray(min: Long, max: Long, size: Int = -1): LongArray {
        val arraySize = if (size < 0) aTinyInt() else size
        return LongArray(arraySize, { aLong(min, max) })
    }

    /**
     * @param mean the mean value of the distribution (default : 0)
     * @param standardDeviation the standard deviation value of the distribution (default : 100)
     * @param size the size of the array, or -1 for a random size
     * @return an array of long with a gaussian distribution
     */
    @JvmOverloads
    fun aLongArrayWithDistribution(mean: Long = 0, standardDeviation: Long = 100, size: Int = -1): LongArray {
        val arraySize = if (size < 0) aTinyInt() else size
        return LongArray(arraySize, { aGaussianLong(mean, standardDeviation) })
    }

    /**
     * @param constraint a constraint on the floats to forge
     * @param size the size of the array, or -1 for a random size
     * * @return an array of float
     */
    @JvmOverloads
    fun aFloatArray(constraint: FloatConstraint, size: Int = -1): FloatArray {
        val arraySize = if (size < 0) aTinyInt() else size
        return FloatArray(arraySize, { aFloat(constraint) })
    }

    /**
     * @param min the minimum value for all floats in the array
     * @param max the maximum value for all floats in the array
     * @param size the size of the array, or -1 for a random size
     * @return an array of float
     */
    @JvmOverloads
    fun aFloatArray(min: Float, max: Float, size: Int = -1): FloatArray {
        val arraySize = if (size < 0) aTinyInt() else size
        return FloatArray(arraySize, { aFloat(min, max) })
    }

    /**
     * @param mean the mean value of the distribution (default : 0f)
     * @param standardDeviation the standard deviation value of the distribution (default : 1f)
     * @param size the size of the array, or -1 for a random size
     * @return an array of int with a gaussian distribution
     */
    @JvmOverloads
    fun aFloatArrayWithDistribution(mean: Float = 0f, standardDeviation: Float = 1f, size: Int = -1): FloatArray {
        val arraySize = if (size < 0) aTinyInt() else size
        return FloatArray(arraySize, { aGaussianFloat(mean, standardDeviation) })
    }

    /**
     * @param constraint a constraint on the doubles to forge
     * @param size the size of the array, or -1 for a random size
     * * @return an array of double
     */
    @JvmOverloads
    fun aDoubleArray(constraint: DoubleConstraint, size: Int = -1): DoubleArray {
        val arraySize = if (size < 0) aTinyInt() else size
        return DoubleArray(arraySize, { aDouble(constraint) })
    }

    /**
     * @param min the minimum value for all doubles in the array
     * @param max the maximum value for all doubles in the array
     * @param size the size of the array, or -1 for a random size
     * @return an array of double
     */
    @JvmOverloads
    fun aDoubleArray(min: Double, max: Double, size: Int = -1): DoubleArray {
        val arraySize = if (size < 0) aTinyInt() else size
        return DoubleArray(arraySize, { aDouble(min, max) })
    }

    /**
     * @param mean the mean value of the distribution (default : 0.0)
     * @param standardDeviation the standard deviation value of the distribution (default : 1.0)
     * @param size the size of the array, or -1 for a random size
     * @return an array of int with a gaussian distribution
     */
    @JvmOverloads
    fun aDoubleArrayWithDistribution(mean: Double = 0.0, standardDeviation: Double = 1.0, size: Int = -1): DoubleArray {
        val arraySize = if (size < 0) aTinyInt() else size
        return DoubleArray(arraySize, { aGaussianDouble(mean, standardDeviation) })
    }

    /**
     * @param constraint a constraint on the chars to forge
     * @param size the size of the array, or -1 for a random size
     * * @return an array of char
     */
    @JvmOverloads
    fun aCharArray(constraint: CharConstraint, case: Case = Case.ANY, size: Int = -1): CharArray {
        val arraySize = if (size < 0) aTinyInt() else size
        return CharArray(arraySize, { aChar(constraint, case) })
    }

    /**
     * @param min the minimum value for all chars in the array
     * @param max the maximum value for all chars in the array
     * @param size the size of the array, or -1 for a random size
     * @return an array of char
     */
    @JvmOverloads
    fun aCharArray(min: Char, max: Char, size: Int = -1): CharArray {
        val arraySize = if (size < 0) aTinyInt() else size
        return CharArray(arraySize, { aChar(min, max) })
    }

    /**
     * @param constraint a constraint on the Strings to forge
     * @param size the size of the array, or -1 for a random size
     * * @return an array of Strings
     */
    @JvmOverloads
    fun aStringArray(constraint: StringConstraint, case: Case = Case.ANY, size: Int = -1): Array<String> {
        val arraySize = if (size < 0) aTinyInt() else size
        return Array(arraySize, { aString(constraint, case) })
    }

    /**
     * @param constraint a constraint on the characters of the Strings to forge
     * @param size the size of the array, or -1 for a random size
     * * @return an array of Strings
     */
    @JvmOverloads
    fun aStringArray(constraint: CharConstraint, case: Case = Case.ANY, size: Int = -1): Array<String> {
        val arraySize = if (size < 0) aTinyInt() else size
        return Array(arraySize, { aString(constraint, case) })
    }

    /**
     * @param regex a regex for the Strings to forge
     * @param size the size of the array, or -1 for a random size
     * * @return an array of Strings
     */
    @JvmOverloads
    fun aStringArray(regex: String, size: Int = -1): Array<String> {
        val arraySize = if (size < 0) aTinyInt() else size
        return Array(arraySize, { aStringMatching(regex) })
    }

    /**
     * @param regex a regex for the Strings to forge
     * @param size the size of the array, or -1 for a random size
     * * @return an array of Strings
     */
    @JvmOverloads
    fun aStringArray(regex: Regex, size: Int = -1): Array<String> {
        val arraySize = if (size < 0) aTinyInt() else size
        return Array(arraySize, { aStringMatching(regex) })
    }

    /**
     * Creates a sub list of the given list, with random elements selected from the input
     *
     * @param list       the list to choose from
     * @param outputSize the size of the sublist. If the input list is smaller than the given size,
     * the result will have the size of the input list.
     * @param <T>        The type of elements in the list
     * @return a non null list, with elements picked at random in the input, without duplicates.
     * Note that if the input list contains duplicates, some might appear in the output.
     * The order in the output matches the input order
     */
    fun <T> aSubListOf(list: List<T>, outputSize: Int): List<T> {
        // fast exit : input too short
        if (list.size <= outputSize) return ArrayList(list)

        // fast exit : output <= 0
        if (outputSize <= 0) return emptyList()


        val inputSize = list.size
        val result = ArrayList<T>(outputSize)
        val rng = Random()

        var numberOfItemsToChooseFrom: Int
        var numberOfItemsToSelect = outputSize

        var i = 0
        while (i < inputSize && numberOfItemsToSelect > 0) {

            numberOfItemsToChooseFrom = inputSize - i
            val probabilityToSelectCurrent = numberOfItemsToSelect.toDouble() / numberOfItemsToChooseFrom.toDouble()
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
     * Creates a sub set of the given set, with random elements selected from the input
     *
     * @param set       the set to choose from
     * @param outputSize the size of the sublist. If the input set is smaller than the given size,
     * the result will have the size of the input set.
     * @param <T>        The type of elements in the set
     * @return a non null set, with elements picked at random in the input, without duplicates.
     */
    fun <T> aSubSetOf(set: Set<T>, outputSize: Int): Set<T> {
        // fast exit : input too short
        if (set.size <= outputSize) return HashSet(set)

        // fast exit : output <= 0
        if (outputSize <= 0) return emptySet()

        val setList = set.toList()
        val inputSize = set.size
        val result = HashSet<T>(outputSize)
        val rng = Random()

        var numberOfItemsToChooseFrom: Int
        var numberOfItemsToSelect = outputSize

        var i = 0
        while (i < inputSize && numberOfItemsToSelect > 0) {

            numberOfItemsToChooseFrom = inputSize - i
            val probabilityToSelectCurrent = numberOfItemsToSelect.toDouble() / numberOfItemsToChooseFrom.toDouble()
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
     * Creates a random sized list
     */
    fun <T> aList(size: Int = -1, creator: (Forger) -> T): List<T> {
        val listSize = if (size < 0) aTinyInt() else size
        val list = ArrayList<T>(listSize)

        for (i in 0 until listSize) {
            list.add(creator(this))
        }

        return list
    }

    /**
     * Shuffles a list (like a deck of card)
     * @param list the list to shuffle
     * @return a new list with the same elements as the input, but in a random order
     */
    inline fun <reified T> shuffle(list: List<T>): List<T> {
        // fast exit : input is empty
        if (list.isEmpty()) return emptyList()

        val result = list.toTypedArray()


        for (i in 0..(list.size - 2)) {
            val j = anInt(i, list.size)
            val temp = result[i]
            result[i] = result[j]
            result[j] = temp
        }

        return listOf(*result)
    }

    // endregion

    // region Enum

    /**
     * @param enumClass an Enum class
     * @return an element “randomly” picked in the enum values
     */
    fun <E : Enum<E>> aValueFrom(enumClass: Class<E>): E {
        return anElementFrom(*enumClass.enumConstants)
    }

    // endregion

    // region Internal

    internal fun preconditionException(message: String): Nothing {
        if (ignorePreconditionsErrors) {
            throw AssumptionViolatedException(message)
        } else {
            throw IllegalArgumentException(message)
        }
    }

    internal fun unsupportedFeature(message: String): Nothing {
        throw UnsupportedOperationException("$message. You can report an issue or submit a PR to https://github.com/xgouchet/Elmyr/")
    }


    /**
     * @param separator the char/string to use as separator. Defaults to the current platform's separator.
     * @param roots the possible path roots
     * @param maxPathSize the maximum size for a full path
     * @param maxFileSize the maximum size for a file/directory name
     * @param forbiddenChars an array of reserved characters forbidden in a directory or file name
     * @return a String matching a standard path format
     */
    internal fun aPath(separator: String = File.separator,
                       roots: List<String>,
                       maxPathSize: Int,
                       maxFileSize: Int,
                       forbiddenChars: CharArray? = null,
                       reservedFilenames: List<String>? = null): String {
        val builder = StringBuilder()
        var segments = 0

        if (roots.isNotEmpty()) {
            builder.append(anElementFrom(roots))
                    .append(separator)
            segments++
        }

        val isFile = aBool()
        val fileSize = if (isFile) anInt(3, maxFileSize) else 0
        val maxSize = (maxPathSize - fileSize - separator.length)
        val reserved = reservedFilenames ?: emptyList()

        while ((builder.length < maxSize) or !aBool(segments.toFloat() / 10.0f)) {
            val max = min(maxFileSize, maxSize - builder.length - separator.length)
            if (max <= 1) break

            val segmentSize = anInt(1, max)
            // TODO maybe extend the charset to full UTF 8 ?
            var folder: String
            do {
                folder = aString(CharConstraint.ASCII, size = segmentSize, forbiddenChars = forbiddenChars)
            } while (folder in reserved)

            builder.append(folder)
                    .append(separator)
            segments++
        }

        if (isFile) {

        }

        return builder.toString()
    }


    private fun getWordSize(size: Int): Int {
        return if (size > 0) size else aTinyInt()
    }


    // endregion

    companion object {

        // Int
        const internal val TINY_THRESHOLD = 0x20
        const internal val SMALL_THRESHOLD = 0x100
        const internal val BIG_THRESHOLD = 0x10000
        const internal val HUGE_THRESHOLD = 0x1000000

        @JvmField internal val MEAN_THRESHOLD_INT = Math.round(Math.sqrt(Int.MAX_VALUE.toDouble())).toInt()

        // LONG
        @JvmField internal val ONE_YEAR = TimeUnit.DAYS.toMillis(365)
        @JvmField internal val MEAN_THRESHOLD_LONG = Math.round(Math.sqrt(Long.MAX_VALUE.toDouble()))

        // FLOAT
        @JvmField internal val MEAN_THRESHOLD_FLOAT = Math.sqrt(Float.MAX_VALUE.toDouble()).toFloat()

        // DOUBLE
        @JvmField internal val MEAN_THRESHOLD_DOUBLE = Math.sqrt(Double.MAX_VALUE)

        // Char
        const internal val MIN_PRINTABLE = 0x20.toChar()
        const internal val MAX_ASCII = 0x7F.toChar()
        const internal val MAX_ASCII_EXTENDED = 0xFF.toChar()
        const internal val MAX_UTF8 = 0xD800.toChar()

        @JvmField internal val ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray()
        @JvmField internal val ALPHA_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
        @JvmField internal val ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz".toCharArray()

        @JvmField internal val ALPHA_NUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_0123456789".toCharArray()
        @JvmField internal val ALPHA_NUM_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789".toCharArray()
        @JvmField internal val ALPHA_NUM_LOWER = "abcdefghijklmnopqrstuvwxyz_0123456789".toCharArray()

        @JvmField internal val HEXA = "abcdefABCDEF0123456789".toCharArray()
        @JvmField internal val HEXA_UPPER = "ABCDEF0123456789".toCharArray()
        @JvmField internal val HEXA_LOWER = "abcdef0123456789".toCharArray()

        @JvmField internal val VOWEL = "aeiouyAEIOUY".toCharArray()
        @JvmField internal val VOWEL_UPPER = "AEIOUY".toCharArray()

        @JvmField internal val VOWEL_LOWER = "aeiouy".toCharArray()
        @JvmField internal val CONSONANT = "ZRTPQSDFGHJKLMWXCVBNzrtpqsdfghjklmwxcvbn".toCharArray()
        @JvmField internal val CONSONANT_UPPER = "ZRTPQSDFGHJKLMWXCVBN".toCharArray()

        @JvmField internal val CONSONANT_LOWER = "zrtpqsdfghjklmwxcvbn".toCharArray()

        @JvmField internal val DIGIT = "0123456789".toCharArray()

        @JvmField internal val WHITESPACE = "\t\n\r ".toCharArray()


    }

}