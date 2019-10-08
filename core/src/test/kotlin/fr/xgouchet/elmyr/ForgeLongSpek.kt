package fr.xgouchet.elmyr

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class ForgeLongSpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        val testRepeatCountSmall = 1024

        beforeEachTest {
            seed = System.nanoTime()
            forge.seed = seed
        }

        // region Long in Range

        context("forging longs ") {

            it("fails if min > max") {
                val min = forge.aPositiveLong(strict = true)
                val max = forge.aNegativeLong(strict = true)
                throws<IllegalArgumentException> {
                    forge.aLong(min, max)
                }
            }

            it("fails if min == max") {
                val min = forge.aLong()
                throws<IllegalArgumentException> {
                    forge.aLong(min, min)
                }
            }

            it("forges a long in a specified range") {
                val min = forge.aLong()
                val max = forge.aLong(min = min + 1)

                repeat(testRepeatCountSmall) {
                    val long = forge.aLong(min, max)
                    assertThat(long)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                }
            }

            it("forges a long in minimal range") {
                val min = forge.aLong()
                val max = min + 1

                repeat(testRepeatCountSmall) {
                    val long = forge.aLong(min, max)
                    assertThat(long)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                }
            }

            it("forges a long above a min") {
                val min = forge.aNegativeLong()

                repeat(testRepeatCountSmall) {
                    val long = forge.aLong(min = min)
                    assertThat(long)
                            .isGreaterThanOrEqualTo(min)
                }
            }

            it("forges a long below a max") {
                val max = forge.aPositiveLong()

                repeat(testRepeatCountSmall) {
                    val long = forge.aLong(max = max)
                    assertThat(long)
                            .isLessThan(max)
                }
            }
        }

        // endregion

        // region Long with sign

        context("forging longs with sign") {

            it("forges a positive long") {
                repeat(testRepeatCountSmall) {
                    val long = forge.aPositiveLong(/*strict = false*/)
                    assertThat(long)
                            .isGreaterThanOrEqualTo(0)
                }
            }

            it("forges a strictly positive long") {
                repeat(testRepeatCountSmall) {
                    val long = forge.aPositiveLong(strict = true)
                    assertThat(long)
                            .isGreaterThan(0)
                }
            }

            it("forges a negative long") {
                repeat(testRepeatCountSmall) {
                    val long = forge.aNegativeLong(strict = false)
                    assertThat(long)
                            .isLessThanOrEqualTo(0)
                }
            }

            it("forges a strictly negative long") {
                repeat(testRepeatCountSmall) {
                    val long = forge.aNegativeLong(/*strict = true*/)
                    assertThat(long)
                            .isLessThan(0)
                }
            }
        }

        // endregion

        // region Long in Gaussian distribution

        context("forging longs with gaussian") {

            it("fails if standard deviation < 0") {
                val mean = forge.aLong(-1000L, 1000L)
                val stDev = forge.aNegativeLong(true)
                throws<IllegalArgumentException> {
                    forge.aGaussianLong(mean, stDev)
                }
            }

            it("fails if mean > MAX_VALUE/2") {
                val mean = forge.aLong(min = Long.MAX_VALUE / 2)
                val stDev = forge.aLong(1, 10)
                throws<IllegalArgumentException> {
                    forge.aGaussianLong(mean, stDev)
                }
            }

            it("fails if mean < -MAX_VALUE/2") {
                val mean = forge.aLong(max = -Long.MAX_VALUE / 2)
                val stDev = forge.aLong(1, 10)
                throws<IllegalArgumentException> {
                    forge.aGaussianLong(mean, stDev)
                }
            }

            it("forges a gaussian distributed long with defaults") {
                val mean = 0L
                val stdev = 100L

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianLong() }
            }

            it("forges a gaussian distributed long with defaults stdev") {
                val mean = forge.aLong(-1000L, 1000L)
                val stdev = 100L

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianLong(mean) }
            }

            it("forges a gaussian distributed long with defaults mean") {
                val mean = 0L
                val stdev = forge.aLong(10, 500)

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianLong(standardDeviation = stdev) }
            }

            it("forges a gaussian distributed long") {
                val mean = forge.aLong(-1000, 1000)
                val stdev = forge.aLong(10, 500)

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianLong(mean, stdev) }
            }

            it("forges mean when standard deviation is 0") {
                val mean = forge.aLong(-1000, 1000)
                val stdev = 0L

                repeat(testRepeatCountSmall) {
                    val result = forge.aGaussianLong(mean, stdev)
                    assertThat(result).isEqualTo(mean)
                }
            }
        }

        // endregion
    }
})
