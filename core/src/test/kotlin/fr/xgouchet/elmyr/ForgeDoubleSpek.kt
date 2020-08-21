package fr.xgouchet.elmyr

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ForgeDoubleSpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        val testRepeatCountSmall = 16

        beforeEachTest {
            seed = Forge.seed()
            forge.seed = seed
        }

        // region Doubles in range

        context("forging doubles in range") {

            it("fails if min > max") {
                val min = forge.aPositiveDouble(strict = true)
                val max = forge.aNegativeDouble(strict = true)
                throws<IllegalArgumentException> {
                    forge.aDouble(min, max)
                }
            }

            it("forges a double in a specified range") {
                val min = forge.aDouble(-Forge.MEAN_THRESHOLD_DOUBLE, 0.0)
                val max = forge.aDouble(0.0, Forge.MEAN_THRESHOLD_DOUBLE)

                assertThat(max).isGreaterThan(min)

                repeat(16) {
                    val double = forge.aDouble(min, max)
                    assertThat(double)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                }
            }

            it("forges a double in minimal range") {
                val min = forge.aDouble(-100000.0, 100000.0)
                val max = min

                repeat(16) {
                    val double = forge.aDouble(min, max)
                    assertThat(double)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThanOrEqualTo(max)
                }
            }

            it("forges a double in whole range") {
                repeat(16) {
                    val double = forge.aDouble()
                    assertThat(double)
                            .isNotIn(Double.NEGATIVE_INFINITY, Double.NaN, Double.POSITIVE_INFINITY)
                }
            }

            it("forges a double above a min") {
                val min = forge.aDouble(-100000.0, 100000.0)

                repeat(testRepeatCountSmall) {
                    val double = forge.aDouble(min = min)
                    assertThat(double)
                            .isGreaterThanOrEqualTo(min)
                }
            }

            it("forges a double below a max") {
                val max = forge.aDouble(-100000.0, 100000.0)

                repeat(testRepeatCountSmall) {
                    val double = forge.aDouble(max = max)
                    assertThat(double)
                            .isLessThan(max)
                }
            }
        }

        // endregion

        // region Doubles with sign

        context("forging doubles with sign") {

            it("forges a positive double") {
                repeat(16) {
                    val double = forge.aPositiveDouble(/*strict = false*/)
                    assertThat(double)
                            .isGreaterThanOrEqualTo(0.0)
                }
            }

            it("forges a strictly positive double") {
                repeat(16) {
                    val double = forge.aPositiveDouble(strict = true)
                    assertThat(double)
                            .isGreaterThan(0.0)
                }
            }

            it("forges a negative double") {
                repeat(16) {
                    val double = forge.aNegativeDouble(strict = false)
                    assertThat(double)
                            .isLessThanOrEqualTo(0.0)
                }
            }

            it("forges a strictly negative double") {
                repeat(16) {
                    val double = forge.aNegativeDouble(/*strict = true*/)
                    assertThat(double)
                            .isLessThan(0.0)
                }
            }
        }

        // endregion

        // region Doubles in Gaussian Distribution

        context("forging doubles in gaussian distribution") {

            it("fails if standard dev < 0") {
                val mean = forge.aDouble(0.0, Forge.SMALL_THRESHOLD.toDouble())
                val stDev = forge.aNegativeDouble(true)
                throws<IllegalArgumentException> {
                    forge.aGaussianDouble(mean, stDev)
                }
            }

            it("fails if mean > MAX_VALUE/2") {
                val mean = forge.aDouble(min = Double.MAX_VALUE / 2)
                val stDev = forge.aDouble(1.0, 4.0)
                throws<IllegalArgumentException> {
                    forge.aGaussianDouble(mean, stDev)
                }
            }

            it("fails if mean < -MAX_VALUE/2") {
                val mean = forge.aDouble(max = -Double.MAX_VALUE / 2)
                val stDev = forge.aDouble(1.0, 4.0)
                throws<IllegalArgumentException> {
                    forge.aGaussianDouble(mean, stDev)
                }
            }

            it("forges a gaussian distributed double with defaults") {
                val mean = 0.0
                val stdev = 1.0

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianDouble() }
            }

            it("forges a gaussian distributed double with defaults stdev") {
                val mean = forge.aDouble(-1000.0, 1000.0)
                val stdev = 1.0

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianDouble(mean) }
            }

            it("forges a gaussian distributed double with defaults mean") {
                val mean = 0.0
                val stdev = forge.aDouble(10.0, 500.0)

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianDouble(standardDeviation = stdev) }
            }

            it("forges a gaussian distributed double") {
                val mean = forge.aDouble(-1000.0, 1000.0)
                val stdev = forge.aDouble(10.0, 500.0)

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianDouble(mean, stdev) }
            }

            it("forges mean when standard deviation is 0") {
                val mean = forge.aDouble(-1000.0, 1000.0)
                val stdev = 0.0

                repeat(testRepeatCountSmall) {
                    val result = forge.aGaussianDouble(mean, stdev)
                    assertThat(result).isEqualTo(mean)
                }
            }
        }

        // endregion
    }
})
