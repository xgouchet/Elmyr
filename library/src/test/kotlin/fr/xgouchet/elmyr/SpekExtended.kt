package fr.xgouchet.elmyr

import org.assertj.core.api.Java6Assertions.assertThat
import org.assertj.core.api.Java6Assertions.within

/**
 * @author Xavier F. Gouchet
 */

/**
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
        if (!matches && ex != null) throw AssertionError("block should have thrown a ${T::class.simpleName}, but threw a ${ex.javaClass.simpleName}")
    }
}

fun verifyGaussianDistribution(
    count: Int,
    expectedMean: Double,
    expectedStandardDev: Double,
    provider: (Int) -> Double
) {
    var sum = 0.0
    var squareSum = 0.0

    for (i in 0 until count) {
        val x = provider(i)
        sum += x
        squareSum += x * x
    }

    val computedMean = sum / count
    val computedStDev = Math.sqrt(Math.abs((squareSum - (count * expectedMean * expectedMean))) / (count - 1.0))
    assertThat(computedMean)
            .isCloseTo(expectedMean, within(expectedStandardDev))
    assertThat(computedStDev)
            .isCloseTo(expectedStandardDev, within(expectedStandardDev * 10))
}

fun verifyProbability(
    count: Int,
    expectedProbability: Double,
    provider: () -> Boolean
) {

    var countTrue = 0.0

    repeat(count) { if (provider()) countTrue++ }

    assertThat(countTrue / count)
            .isCloseTo(expectedProbability, within(0.1))
}
