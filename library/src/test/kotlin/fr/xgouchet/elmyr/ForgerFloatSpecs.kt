package fr.xgouchet.elmyr

import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Java6Assertions.assertThat
import org.assertj.core.api.Java6Assertions.within
import java.lang.Math.abs
import java.lang.Math.sqrt

/**
 * @author Xavier F. Gouchet
 */
class ForgerFloatSpecs : FeatureSpec() {

    init {

        feature("An Number Forger uses a seed") {

            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)
            val data = FloatArray(16) { forger.aFloat(min = it.toFloat()) }

            scenario("Reproduce data with another forger using the same seed") {
                val otherForger = Forger()
                otherForger.reset(seed)
                val otherData = FloatArray(16) { otherForger.aFloat(min = it.toFloat()) }

                assertThat(otherData)
                        .containsExactly(*data)
            }

            scenario("Reproduce data with same forger reset with the same seed") {
                forger.reset(seed)
                val otherData = FloatArray(16) { forger.aFloat(min = it.toFloat()) }

                assertThat(otherData)
                        .containsExactly(*data)
            }


            scenario("Produce different data with different seed") {
                val otherSeed = System.nanoTime()
                forger.reset(otherSeed)
                val otherData = FloatArray(16) { forger.aFloat(min = it.toFloat()) }

                assertThat(otherData)
                        .isNotEqualTo(data)
            }
        }

        feature("An Number Forger ensure coherent input") {
            val forger = Forger()
            forger.reset(System.nanoTime())

            scenario("Fail if min > max") {
                val min = forger.aPositiveFloat(strict = true)
                val max = forger.aNegativeFloat(strict = true)
                shouldThrow<IllegalArgumentException> {
                    forger.aFloat(min, max)
                }
            }

            scenario("Fail if standard dev < 0") {
                val mean = forger.aFloat()
                val stDev = forger.aNegativeFloat(true)
                shouldThrow<IllegalArgumentException> {
                    forger.aGaussianFloat(mean, stDev)
                }
            }

            scenario("Fail if mean > MAX_VALUE/2") {
                val mean = forger.aFloat(min = Float.MAX_VALUE / 2)
                val stDev = forger.aFloat(1f, 4f)
                shouldThrow<IllegalArgumentException> {
                    forger.aGaussianFloat(mean, stDev)
                }
            }

            scenario("Fail if mean < -MAX_VALUE/2") {
                val mean = forger.aFloat(max = -Float.MAX_VALUE / 2)
                val stDev = forger.aFloat(1f, 4f)
                shouldThrow<IllegalArgumentException> {
                    forger.aGaussianFloat(mean, stDev)
                }
            }
        }

        feature("An Number Forger produces meaningful floats") {

            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)

            scenario("Produce an float in a specified range") {
                val min = forger.aFloat(-100000f, 100000f)
                val max = forger.aFloat(min + 1, 150000f)

                assertThat(max).isGreaterThan(min)

                repeat(16, {
                    val float = forger.aFloat(min, max)
                    assertThat(float)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                })
            }

            scenario("Produce an float in a small range") {
                val min = forger.aFloat(-100000f, 100000f)
                val max = min + 10

                repeat(16, {
                    val float = forger.aFloat(min, max)
                    assertThat(float)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThanOrEqualTo(max)
                })
            }

            scenario("Produces a positive float") {
                repeat(16, {
                    val float = forger.aPositiveFloat()
                    assertThat(float)
                            .isGreaterThanOrEqualTo(0.0f)
                })
            }

            scenario("Produces a strictly positive float") {
                repeat(16, {
                    val float = forger.aPositiveFloat(strict = true)
                    assertThat(float)
                            .isGreaterThan(0.0f)
                })
            }

            scenario("Produces a negative float") {
                repeat(16, {
                    val float = forger.aNegativeFloat()
                    assertThat(float)
                            .isLessThanOrEqualTo(0.0f)
                })
            }

            scenario("Produces a strictly negative float") {
                repeat(16, {
                    val float = forger.aNegativeFloat(strict = true)
                    assertThat(float)
                            .isLessThan(0.0f)
                })
            }

            scenario("Produces a gaussian distributed float") {

                val mean = forger.aFloat(-1000f, 1000f)
                val stdev = forger.aFloat(10f, 500f)

                val count = 1024
                var sum = 0f
                var squareSum = 0f

                repeat(count, {
                    val float = forger.aGaussianFloat(mean, stdev)
                    sum += float
                    squareSum += float * float
                })

                val computedMean = sum / count
                val computedStDev = sqrt(abs((squareSum - (count * mean * mean))) / (count - 1.0)).toFloat()
                assertThat(computedMean)
                        .isCloseTo(mean, within(stdev / 10.0f))
                assertThat(computedStDev)
                        .isCloseTo(stdev, within(stdev / 2.0f))
            }

        }


    }
}