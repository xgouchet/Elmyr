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
class ForgerIntSpecs : FeatureSpec() {

    init {

        feature("An Number Forger uses a seed") {

            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)
            val data = IntArray(16) { forger.anInt(min = it) }

            scenario("Reproduce data with another forger using the same seed") {
                val otherForger = Forger()
                otherForger.reset(seed)
                val otherData = IntArray(16) { otherForger.anInt(min = it) }

                assertThat(otherData)
                        .containsExactly(*data)
            }

            scenario("Reproduce data with same forger reset with the same seed") {
                forger.reset(seed)
                val otherData = IntArray(16) { forger.anInt(min = it) }

                assertThat(otherData)
                        .containsExactly(*data)
            }


            scenario("Produce different data with different seed") {
                val otherSeed = System.nanoTime()
                forger.reset(otherSeed)
                val otherData = IntArray(16) { forger.anInt(min = it) }

                assertThat(otherData)
                        .isNotEqualTo(data)
            }
        }

        feature("An Number Forger ensure coherent input") {
            val forger = Forger()
            forger.reset(System.nanoTime())

            scenario("Fail if min > max") {
                val min = forger.aPositiveInt(strict = true)
                val max = forger.aNegativeInt(strict = true)
                shouldThrow<IllegalArgumentException> {
                    forger.anInt(min, max)
                }
            }

            scenario("Fail if min == max") {
                val min = forger.anInt()
                shouldThrow<IllegalArgumentException> {
                    forger.anInt(min, min)
                }
            }

            scenario("Fail if standard deviation < 0") {
                val mean = forger.aSmallInt()
                val stDev = forger.aNegativeInt(true)
                shouldThrow<IllegalArgumentException> {
                    forger.aGaussianInt(mean, stDev)
                }
            }

            scenario("Fail if mean > MAX_VALUE/2") {
                val mean = forger.anInt(min = Int.MAX_VALUE / 2)
                val stDev = forger.anInt(1, 10)
                shouldThrow<IllegalArgumentException> {
                    forger.aGaussianInt(mean, stDev)
                }
            }

            scenario("Fail if mean < -MAX_VALUE/2") {
                val mean = forger.anInt(max = -Int.MAX_VALUE / 2)
                val stDev = forger.anInt(1, 10)
                shouldThrow<IllegalArgumentException> {
                    forger.aGaussianInt(mean, stDev)
                }
            }
        }

        feature("An Number Forger produces meaningful ints") {

            val forger = Forger()
            forger.reset(System.nanoTime())

            scenario("Produce an int in a specified range") {
                val min = forger.anInt()
                val max = forger.anInt(min + 1)

                assertThat(max).isGreaterThan(min)

                repeat(16, {
                    val int = forger.anInt(min, max)
                    assertThat(int)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                })
            }

            scenario("Produce an int in a small range") {
                val min = forger.anInt()
                val max = min + 3

                repeat(16, {
                    val int = forger.anInt(min, max)
                    assertThat(int)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                })
            }

            scenario("Produces a positive int") {
                repeat(16, {
                    val int = forger.aPositiveInt()
                    assertThat(int)
                            .isGreaterThanOrEqualTo(0)
                })
            }

            scenario("Produces a strictly positive int") {
                repeat(16, {
                    val int = forger.aPositiveInt(strict = true)
                    assertThat(int)
                            .isGreaterThan(0)
                })
            }

            scenario("Produces a negative int") {
                repeat(16, {
                    val int = forger.aNegativeInt()
                    assertThat(int)
                            .isLessThanOrEqualTo(0)
                })
            }

            scenario("Produces a strictly negative int") {
                repeat(16, {
                    val int = forger.aNegativeInt(strict = true)
                    assertThat(int)
                            .isLessThan(0)
                })
            }

            scenario("Produces a tiny int") {
                repeat(16, {
                    val int = forger.aTinyInt()
                    assertThat(int)
                            .isGreaterThan(0)
                            .isLessThan(Forger.Companion.TINY_THRESHOLD)
                })
            }

            scenario("Produces a small int") {
                repeat(16, {
                    val int = forger.aSmallInt()
                    assertThat(int)
                            .isGreaterThan(0)
                            .isLessThan(Forger.Companion.SMALL_THRESHOLD)
                })
            }

            scenario("Produces a big int") {
                repeat(16, {
                    val int = forger.aBigInt()
                    assertThat(int)
                            .isGreaterThanOrEqualTo(Forger.Companion.BIG_THRESHOLD)
                })
            }

            scenario("Produces a huge int") {
                repeat(16, {
                    val int = forger.aHugeInt()
                    assertThat(int)
                            .isGreaterThanOrEqualTo(Forger.Companion.HUGE_THRESHOLD)
                })
            }

            scenario("Produces a gaussian distributed float") {
                val mean = forger.anInt(-1000, 1000)
                val stdev = forger.anInt(10, 500)

                val count = 1024
                var sum = 0f
                var squareSum = 0f

                repeat(count, {
                    val int = forger.aGaussianInt(mean, stdev)
                    sum += int
                    squareSum += int * int
                })

                val computedMean = sum / count
                val computedStDev = sqrt(abs(squareSum - (count * mean * mean)) / (count - 1.0)).toFloat()
                assertThat(computedMean)
                        .isCloseTo(mean.toFloat(), within(stdev / 10.0f))
                assertThat(computedStDev)
                        .isCloseTo(stdev.toFloat(), within(stdev / 2.0f))
            }
        }
    }
}