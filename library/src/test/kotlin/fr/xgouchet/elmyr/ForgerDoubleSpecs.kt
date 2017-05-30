package fr.xgouchet.elmyr

import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Java6Assertions
import java.lang.Math.abs
import java.lang.Math.sqrt

/**
 * @author Xavier F. Gouchet
 */
class ForgerDoubleSpecs : FeatureSpec() {

    init {

        feature("An Number Forger uses a seed") {

            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)
            val data = DoubleArray(16) { forger.aDouble(min = it.toDouble()) }

            scenario("Reproduce data with another forger using the same seed") {
                val otherForger = Forger()
                otherForger.reset(seed)
                val otherData = DoubleArray(16) { otherForger.aDouble(min = it.toDouble()) }

                assertThat(otherData)
                        .containsExactly(*data)
            }

            scenario("Reproduce data with same forger reset with the same seed") {
                forger.reset(seed)
                val otherData = DoubleArray(16) { forger.aDouble(min = it.toDouble()) }

                assertThat(otherData)
                        .containsExactly(*data)
            }


            scenario("Produce different data with different seed") {
                val otherSeed = System.nanoTime()
                forger.reset(otherSeed)
                val otherData = DoubleArray(16) { forger.aDouble(min = it.toDouble()) }

                assertThat(otherData)
                        .isNotEqualTo(data)
            }
        }

        feature("An Number Forger ensure coherent input") {
            val forger = Forger()
            forger.reset(System.nanoTime())

            scenario("Fail if min > max") {
                val min = forger.aPositiveDouble(strict = true)
                val max = forger.aNegativeDouble(strict = true)
                shouldThrow<IllegalArgumentException> {
                    forger.aDouble(min, max)
                }
            }

            scenario("Fail if mean > MAX_VALUE/2") {
                val mean = forger.aDouble(min = Double.MAX_VALUE / 2)
                val stDev = forger.aDouble(1.0, 4.0)
                shouldThrow<IllegalArgumentException> {
                    forger.aGaussianDouble(mean, stDev)
                }
            }

            scenario("Fail if mean < -MAX_VALUE/2") {
                val mean = forger.aDouble(max = -Double.MAX_VALUE / 2)
                val stDev = forger.aDouble(1.0, 4.0)
                shouldThrow<IllegalArgumentException> {
                    forger.aGaussianDouble(mean, stDev)
                }
            }
        }

        feature("An Number Forger produces meaningful doubles") {

            val forger = Forger()
            forger.reset(System.nanoTime())

            scenario("Produce an double in a specified range") {
                val min = forger.aDouble(-100000.0, 100000.0)
                val max = forger.aDouble(min + 1, 150000.0)

                Java6Assertions.assertThat(max).isGreaterThan(min)

                repeat(16, {
                    val double = forger.aDouble(min, max)
                    Java6Assertions.assertThat(double)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                })
            }

            scenario("Produce an double in a small range") {
                val min = forger.aDouble(-100000.0, 100000.0)
                val max = min + 10

                repeat(16, {
                    val double = forger.aDouble(min, max)
                    Java6Assertions.assertThat(double)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThanOrEqualTo(max)
                })
            }

            scenario("Produces a positive double") {
                repeat(16, {
                    val double = forger.aPositiveDouble()
                    Java6Assertions.assertThat(double)
                            .isGreaterThanOrEqualTo(0.0)
                })
            }

            scenario("Produces a strictly positive double") {
                repeat(16, {
                    val double = forger.aPositiveDouble(strict = true)
                    Java6Assertions.assertThat(double)
                            .isGreaterThan(0.0)
                })
            }

            scenario("Produces a negative double") {
                repeat(16, {
                    val double = forger.aNegativeDouble()
                    Java6Assertions.assertThat(double)
                            .isLessThanOrEqualTo(0.0)
                })
            }

            scenario("Produces a strictly negative double") {
                repeat(16, {
                    val double = forger.aNegativeDouble(strict = true)
                    Java6Assertions.assertThat(double)
                            .isLessThan(0.0)
                })
            }

            scenario("Produces a gaussian distributed double") {
                val mean = forger.aDouble(-1000.0, 1000.0)
                val stdev = forger.aDouble(10.0, 500.0)

                val count = 1024
                var sum = 0.0
                var squareSum = 0.0

                repeat(count, {
                    val double = forger.aGaussianDouble(mean, stdev)
                    sum += double
                    squareSum += double * double
                })

                val computedMean = sum / count
                val computedStDev = sqrt(abs(squareSum - (count * mean * mean)) / (count - 1.0))
                Java6Assertions.assertThat(computedMean)
                        .isCloseTo(mean, Java6Assertions.within(stdev / 10.0))
                Java6Assertions.assertThat(computedStDev)
                        .isCloseTo(stdev, Java6Assertions.within(stdev / 2.0))
            }
        }


    }
}