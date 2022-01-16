package fr.xgouchet.elmyr

import kotlin.math.abs
import kotlin.math.sqrt
import org.assertj.core.api.Java6Assertions.assertThat
import org.assertj.core.api.Java6Assertions.within

/**
 * @param T the expected type of throwable to thrown
 * @param block the block to test
 * Defines a throws assertion to use in Spek, shamelessly Ctrl+C/Ctrl+V from
 * https://github.com/encodeering/conflate/commit/efc35c4392076b212cca569b0243ebcf8e7b127f
 */
inline fun <reified T : Throwable> throws(block: () -> Unit) {

    var ex: Throwable? = null
    var thrown = false
    var matches = false

    try {
        block()
    } catch (e: Throwable) {
        ex = e
        matches = T::class.isInstance(e)
        thrown = true
    } finally {
        if (!thrown) throw AssertionError("block should have thrown a ${T::class.simpleName}")
        if (!matches && ex != null) {

            throw AssertionError(
                "block should have thrown a ${T::class.simpleName}, " +
                        "but threw a ${ex.javaClass.simpleName}",
                ex
            )
        }
    }
}

/**
 * @param expectedMean the expected mean of the gaussian distribution
 * @param expectedStandardDev the expected standard deviation of the distribution
 * @param provider the operation returning a value
 */
fun verifyGaussianDistribution(
    expectedMean: Int,
    expectedStandardDev: Int,
    provider: (Int) -> Int
) = verifyGaussianDistribution(
    expectedMean.toDouble(),
    expectedStandardDev.toDouble()
) {
    provider(it).toDouble()
}

/**
 * @param expectedMean the expected mean of the gaussian distribution
 * @param expectedStandardDev the expected standard deviation of the distribution
 * @param provider the operation returning a value
 */
fun verifyGaussianDistribution(
    expectedMean: Long,
    expectedStandardDev: Long,
    provider: (Int) -> Long
) = verifyGaussianDistribution(
    expectedMean.toDouble(),
    expectedStandardDev.toDouble()
) {
    provider(it).toDouble()
}

/**
 * @param expectedMean the expected mean of the gaussian distribution
 * @param expectedStandardDev the expected standard deviation of the distribution
 * @param provider the operation returning a value
 */
fun verifyGaussianDistribution(
    expectedMean: Float,
    expectedStandardDev: Float,
    provider: (Int) -> Float
) = verifyGaussianDistribution(
    expectedMean.toDouble(),
    expectedStandardDev.toDouble()
) {
    provider(it).toDouble()
}

/**
 * @param expectedMean the expected mean of the gaussian distribution
 * @param expectedStandardDev the expected standard deviation of the distribution
 * @param provider the operation returning a value
 */
fun verifyGaussianDistribution(
    expectedMean: Double,
    expectedStandardDev: Double,
    provider: (Int) -> Double
) {
    val count = 2048
    var sum = 0.0
    var squareSum = 0.0
    val maxDeviation = expectedStandardDev * 3.05 // add some margin for float precision errors

    for (i in 0 until count) {
        val x = provider(i)
        assertThat(x)
            .isBetween(expectedMean - maxDeviation, expectedMean + maxDeviation)
        sum += x
        squareSum += x * x
    }

    val computedMean = sum / count
    assertThat(computedMean)
        .isCloseTo(expectedMean, within(expectedStandardDev))

    val d = squareSum - (count * computedMean * computedMean)
    val computedStDev = sqrt(abs(d) / (count - 1.0))

    assertThat(computedStDev)
        .isGreaterThanOrEqualTo(0.0)

    if (expectedStandardDev <= 1.0) {
        assertThat(computedStDev)
            .overridingErrorMessage(
                "Expected a standard deviation of " +
                        "<$expectedStandardDev> but was <$computedStDev>"
            )
            .isLessThanOrEqualTo(expectedStandardDev * 3.0)
    } else if (expectedStandardDev <= 3.0) {
        assertThat(computedStDev)
            .overridingErrorMessage(
                "Expected a standard deviation of " +
                        "<$expectedStandardDev> but was <$computedStDev>"
            )
            .isGreaterThan(expectedStandardDev / 2.0)
            .isLessThanOrEqualTo(expectedStandardDev * 5.0)
    } else {
        assertThat(computedStDev)
            .overridingErrorMessage(
                "Expected a standard deviation of " +
                        "<$expectedStandardDev> but was <$computedStDev>"
            )
            .isGreaterThan(sqrt(expectedStandardDev))
            .isLessThanOrEqualTo(expectedStandardDev * 8.0)
    }
}

/**
 * @param expectedProbability the expected probability that the operation returns true
 * @param operation an operation returning a boolean
 */
fun verifyProbability(
    expectedProbability: Float,
    operation: () -> Boolean
) = verifyProbability(expectedProbability.toDouble(), operation)

/**
 * @param expectedProbability the expected probability that the operation returns true
 * @param operation an operation returning a boolean
 */
fun verifyProbability(
    expectedProbability: Double,
    operation: () -> Boolean
) {

    val count = 512
    var countTrue = 0.0

    repeat(count) { if (operation()) countTrue++ }

    assertThat(countTrue / count)
        .isCloseTo(expectedProbability, within(0.1))
}
