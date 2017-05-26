package fr.xgouchet.elmyr

import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Java6Assertions.assertThat
import org.assertj.core.api.Java6Assertions.within

/**
 * @author Xavier F. Gouchet
 */
class ForgerLongSpecs : FeatureSpec() {

    init {

        feature("An Number Forger uses a seed") {

            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)
            val data = LongArray(16) { forger.aLong(min = it.toLong()) }

            scenario("Reproduce data with another forger using the same seed") {
                val otherForger = Forger()
                otherForger.reset(seed)
                val otherData = LongArray(16) { otherForger.aLong(min = it.toLong()) }

                assertThat(otherData)
                        .containsExactly(*data)
            }

            scenario("Reproduce data with same forger reset with the same seed") {
                forger.reset(seed)
                val otherData = LongArray(16) { forger.aLong(min = it.toLong()) }

                assertThat(otherData)
                        .containsExactly(*data)
            }


            scenario("Produce different data with different seed") {
                val otherSeed = System.nanoTime()
                forger.reset(otherSeed)
                val otherData = LongArray(16) { forger.aLong(min = it.toLong()) }

                assertThat(otherData)
                        .isNotEqualTo(data)
            }
        }

        feature("An Number Forger ensure coherent input") {
            val forger = Forger()
            forger.reset(System.nanoTime())

            scenario("Fail if min > max") {
                val min = forger.aPositiveLong(strict = true)
                val max = forger.aNegativeLong(strict = true)
                shouldThrow<IllegalArgumentException> {
                    forger.aLong(min, max)
                }
            }

            scenario("Fail if min == max") {
                val min = forger.aLong()
                shouldThrow<IllegalArgumentException> {
                    forger.aLong(min, min)
                }
            }

            scenario("Fail if standard deviation < 0") {
                val mean = forger.aLong(1, Forger.SMALL_THRESHOLD.toLong())
                val stDev = forger.aNegativeLong(true)
                shouldThrow<IllegalArgumentException> {
                    forger.aGaussianLong(mean, stDev)
                }
            }
        }

        feature("An Number Forger produces meaningful longs") {

            val forger = Forger()
            forger.reset(System.nanoTime())

            scenario("Produce an long in a specified range") {
                val min = forger.aLong()
                val max = forger.aLong(min + 1)

                assertThat(max).isGreaterThan(min)

                repeat(16, {
                    val long = forger.aLong(min, max)
                    assertThat(long)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                })
            }

            scenario("Produce an long in a small range") {
                val min = forger.aLong()
                val max = min + 3

                repeat(16, {
                    val long = forger.aLong(min, max)
                    assertThat(long)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                })
            }

            scenario("Produces a positive long") {
                repeat(16, {
                    val long = forger.aPositiveLong()
                    assertThat(long)
                            .isGreaterThanOrEqualTo(0)
                })
            }

            scenario("Produces a strictly positive long") {
                repeat(16, {
                    val long = forger.aPositiveLong(strict = true)
                    assertThat(long)
                            .isGreaterThan(0)
                })
            }

            scenario("Produces a negative long") {
                repeat(16, {
                    val long = forger.aNegativeLong()
                    assertThat(long)
                            .isLessThanOrEqualTo(0)
                })
            }

            scenario("Produces a strictly negative long") {
                repeat(16, {
                    val long = forger.aNegativeLong(strict = true)
                    assertThat(long)
                            .isLessThan(0)
                })
            }

            scenario("Produces a gaussian distributed float") {
                val mean = forger.aLong(-1000, 1000)
                val stdev = forger.aLong(10, 500)

                val count = 1024
                var sum = 0f
                var squareSum = 0f

                repeat(count, {
                    val long = forger.aGaussianLong(mean, stdev)
                    sum += long
                    squareSum += long * long
                })

                val computedMean = sum / count
                val computedStDev = Math.sqrt((squareSum - (count * mean * mean)) / (count - 1.0)).toFloat()
                assertThat(computedMean)
                        .isCloseTo(mean.toFloat(), within(stdev / 10.0f))
                assertThat(computedStDev)
                        .isCloseTo(stdev.toFloat(), within(stdev / 2.0f))
            }
        }
    }
}