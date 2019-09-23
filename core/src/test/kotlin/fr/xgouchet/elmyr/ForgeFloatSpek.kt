package fr.xgouchet.elmyr

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class ForgeFloatSpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        val testRepeatCountSmall = 16

        beforeEachTest {
            seed = System.nanoTime()
            forge.seed = seed
        }

        // region Floats in range

        context("forging floats in range") {

            it("fails if min > max") {
                val min = forge.aPositiveFloat(strict = true)
                val max = forge.aNegativeFloat(strict = true)
                throws<IllegalArgumentException> {
                    forge.aFloat(min, max)
                }
            }

            it("forges an float in a specified range") {
                val min = forge.aFloat(-Forge.MEAN_THRESHOLD_FLOAT, 0f)
                val max = forge.aFloat(0f, Forge.MEAN_THRESHOLD_FLOAT)

                assertThat(max).isGreaterThan(min)

                repeat(16) {
                    val float = forge.aFloat(min, max)
                    assertThat(float)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                }
            }

            it("forges an float in minimal range") {
                val min = forge.aFloat(-100000f, 100000f)
                val max = min

                repeat(16) {
                    val float = forge.aFloat(min, max)
                    assertThat(float)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThanOrEqualTo(max)
                }
            }

            it("forges an float in whole range") {
                repeat(16) {
                    val float = forge.aFloat()
                    assertThat(float)
                            .isNotIn(Float.NEGATIVE_INFINITY, Float.NaN, Float.POSITIVE_INFINITY)
                }
            }
        }

        // endregion

        // region Floats with sign

        context("forging floats with sign") {

            it("forges a positive float") {
                repeat(16) {
                    val float = forge.aPositiveFloat(/*strict = false*/)
                    assertThat(float)
                            .isGreaterThanOrEqualTo(0.0f)
                }
            }

            it("forges a strictly positive float") {
                repeat(16) {
                    val float = forge.aPositiveFloat(strict = true)
                    assertThat(float)
                            .isGreaterThan(0.0f)
                }
            }

            it("forges a negative float") {
                repeat(16) {
                    val float = forge.aNegativeFloat(strict = false)
                    assertThat(float)
                            .isLessThanOrEqualTo(0.0f)
                }
            }

            it("forges a strictly negative float") {
                repeat(16) {
                    val float = forge.aNegativeFloat(/*strict = true*/)
                    assertThat(float)
                            .isLessThan(0.0f)
                }
            }
        }

        // endregion

        // region Floats in Gaussian Distribution

        context("forging floats in gaussian distribution") {

            it("fails if standard dev < 0") {
                val mean = forge.aFloat(0f, Forge.SMALL_THRESHOLD.toFloat())
                val stDev = forge.aNegativeFloat(true)
                throws<IllegalArgumentException> {
                    forge.aGaussianFloat(mean, stDev)
                }
            }

            it("fails if mean > MAX_VALUE/2") {
                val mean = forge.aFloat(min = Float.MAX_VALUE / 2)
                val stDev = forge.aFloat(1f, 4f)
                throws<IllegalArgumentException> {
                    forge.aGaussianFloat(mean, stDev)
                }
            }

            it("fails if mean < -MAX_VALUE/2") {
                val mean = forge.aFloat(max = -Float.MAX_VALUE / 2)
                val stDev = forge.aFloat(1f, 4f)
                throws<IllegalArgumentException> {
                    forge.aGaussianFloat(mean, stDev)
                }
            }

            it("forges a gaussian distributed float with defaults") {
                val mean = 0f
                val stdev = 1f

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianFloat() }
            }

            it("forges a gaussian distributed float with defaults stdev") {
                val mean = forge.aFloat(-1000f, 1000f)
                val stdev = 1f

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianFloat(mean) }
            }

            it("forges a gaussian distributed float with defaults mean") {
                val mean = 0f
                val stdev = forge.aFloat(10f, 500f)

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianFloat(standardDeviation = stdev) }
            }

            it("forges a gaussian distributed float") {
                val mean = forge.aFloat(-1000f, 1000f)
                val stdev = forge.aFloat(10f, 500f)

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianFloat(mean, stdev) }
            }

            it("forges mean when standard deviation is 0") {
                val mean = forge.aFloat(-1000f, 1000f)
                val stdev = 0f

                repeat(testRepeatCountSmall) {
                    val result = forge.aGaussianFloat(mean, stdev)
                    assertThat(result).isEqualTo(mean)
                }
            }
        }

        // endregion
    }
})
