package fr.xgouchet.elmyr

import org.assertj.core.api.Java6Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

/**
 * @author Xavier F. Gouchet
 */
class ForgerSpek_Floats : Spek({

    describe("A forger ") {
        val forger = Forger()
        var seed: Long = 0
        val testRepeatCountSmall = 16
        val testRepeatCountHuge = 1024

        beforeEachTest {
            seed = System.nanoTime()
            forger.reset(seed)
        }

        // region Floats

        context("forging floats ") {
            it("fails if min > max") {
                val min = forger.aPositiveFloat(strict = true)
                val max = forger.aNegativeFloat(strict = true)
                throws<IllegalArgumentException> {
                    forger.aFloat(min, max)
                }
            }

            it("fails if standard dev < 0") {
                val mean = forger.aFloat(0f, Forger.SMALL_THRESHOLD.toFloat())
                val stDev = forger.aNegativeFloat(true)
                throws<IllegalArgumentException> {
                    forger.aGaussianFloat(mean, stDev)
                }
            }

            it("fails if mean > MAX_VALUE/2") {
                val mean = forger.aFloat(min = Float.MAX_VALUE / 2)
                val stDev = forger.aFloat(1f, 4f)
                throws<IllegalArgumentException> {
                    forger.aGaussianFloat(mean, stDev)
                }
            }

            it("fails if mean < -MAX_VALUE/2") {
                val mean = forger.aFloat(max = -Float.MAX_VALUE / 2)
                val stDev = forger.aFloat(1f, 4f)
                throws<IllegalArgumentException> {
                    forger.aGaussianFloat(mean, stDev)
                }
            }

            it("forges an float in a specified range") {
                val min = forger.aFloat(-100000f, 100000f)
                val max = forger.aFloat(min + 1, 150000f)

                assertThat(max).isGreaterThan(min)

                repeat(16) {
                    val float = forger.aFloat(min, max)
                    assertThat(float)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                }
            }

            it("forges an float in a small range") {
                val min = forger.aFloat(-100000f, 100000f)
                val max = min + 10

                repeat(16) {
                    val float = forger.aFloat(min, max)
                    assertThat(float)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThanOrEqualTo(max)
                }
            }

            it("forges a positive float") {
                repeat(16) {
                    val float = forger.aPositiveFloat(strict = false)
                    assertThat(float)
                            .isGreaterThanOrEqualTo(0.0f)
                }
            }

            it("forges a strictly positive float") {
                repeat(16) {
                    val float = forger.aPositiveFloat(strict = true)
                    assertThat(float)
                            .isGreaterThan(0.0f)
                }
            }

            it("forges a negative float") {
                repeat(16) {
                    val float = forger.aNegativeFloat(strict = false)
                    assertThat(float)
                            .isLessThanOrEqualTo(0.0f)
                }
            }

            it("forges a strictly negative float") {
                repeat(16) {
                    val float = forger.aNegativeFloat(strict = true)
                    assertThat(float)
                            .isLessThan(0.0f)
                }
            }

            it("forges a gaussian distributed float") {
                val mean = forger.aFloat(-1000f, 1000f)
                val stdev = forger.aFloat(10f, 500f)

                verifyGaussianDistribution(
                        testRepeatCountHuge,
                        mean.toDouble(),
                        stdev.toDouble()
                ) { forger.aGaussianFloat(mean, stdev).toDouble() }
            }

            it("forges mean when standard deviation is 0") {
                val mean = forger.aFloat(-1000f, 1000f)
                val stdev = 0f

                repeat(testRepeatCountSmall) {
                    val result = forger.aGaussianFloat(mean, stdev)
                    assertThat(result).isEqualTo(mean)
                }
            }
        }

        context("forging constrained floats") {

            it("forges a float …") {
                repeat(testRepeatCountSmall) {
                    val float = forger.aFloat(FloatConstraint.ANY)
                    // How do you assert that an float is an float ... ?
                }
            }

            it("forges a positive float") {
                repeat(testRepeatCountSmall) {
                    val float = forger.aFloat(FloatConstraint.POSITIVE)
                    assertThat(float)
                            .isGreaterThanOrEqualTo(0f)
                }
            }

            it("forges a strictly positive float") {
                repeat(testRepeatCountSmall) {
                    val float = forger.aFloat(FloatConstraint.POSITIVE_STRICT)
                    assertThat(float)
                            .isGreaterThan(0f)
                }
            }

            it("forges a negative float") {
                repeat(testRepeatCountSmall) {
                    val float = forger.aFloat(FloatConstraint.NEGATIVE)
                    assertThat(float)
                            .isLessThanOrEqualTo(0f)
                }
            }

            it("forges a strictly negative float") {
                repeat(testRepeatCountSmall) {
                    val float = forger.aFloat(FloatConstraint.NEGATIVE_STRICT)
                    assertThat(float)
                            .isLessThan(0f)
                }
            }
        }

        // endregion

        // region Doubles

        context("forging doubles ") {
            it("fails if min > max") {
                val min = forger.aPositiveDouble(strict = true)
                val max = forger.aNegativeDouble(strict = true)
                throws<IllegalArgumentException> {
                    forger.aDouble(min, max)
                }
            }

            it("fails if standard dev < 0") {
                val mean = forger.aDouble(0.0, Forger.SMALL_THRESHOLD.toDouble())
                val stDev = forger.aNegativeDouble(true)
                throws<IllegalArgumentException> {
                    forger.aGaussianDouble(mean, stDev)
                }
            }

            it("fails if mean > MAX_VALUE/2") {
                val mean = forger.aDouble(min = Double.MAX_VALUE / 2)
                val stDev = forger.aDouble(1.0, 4.0)
                throws<IllegalArgumentException> {
                    forger.aGaussianDouble(mean, stDev)
                }
            }

            it("fails if mean < -MAX_VALUE/2") {
                val mean = forger.aDouble(max = -Double.MAX_VALUE / 2)
                val stDev = forger.aDouble(1.0, 4.0)
                throws<IllegalArgumentException> {
                    forger.aGaussianDouble(mean, stDev)
                }
            }

            it("forges an double in a specified range") {
                val min = forger.aDouble(-100000.0, 100000.0)
                val max = forger.aDouble(min + 1, 150000.0)

                assertThat(max).isGreaterThan(min)

                repeat(16) {
                    val double = forger.aDouble(min, max)
                    assertThat(double)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                }
            }

            it("forges an double in a small range") {
                val min = forger.aDouble(-100000.0, 100000.0)
                val max = min + 10

                repeat(16) {
                    val double = forger.aDouble(min, max)
                    assertThat(double)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThanOrEqualTo(max)
                }
            }

            it("forges a positive double") {
                repeat(16) {
                    val double = forger.aPositiveDouble(strict = false)
                    assertThat(double)
                            .isGreaterThanOrEqualTo(0.0)
                }
            }

            it("forges a strictly positive double") {
                repeat(16) {
                    val double = forger.aPositiveDouble(strict = true)
                    assertThat(double)
                            .isGreaterThan(0.0)
                }
            }

            it("forges a negative double") {
                repeat(16) {
                    val double = forger.aNegativeDouble(strict = false)
                    assertThat(double)
                            .isLessThanOrEqualTo(0.0)
                }
            }

            it("forges a strictly negative double") {
                repeat(16) {
                    val double = forger.aNegativeDouble(strict = true)
                    assertThat(double)
                            .isLessThan(0.0)
                }
            }

            it("forges a gaussian distributed double") {
                val mean = forger.aDouble(-1000.0, 1000.0)
                val stdev = forger.aDouble(10.0, 500.0)

                verifyGaussianDistribution(
                        testRepeatCountHuge,
                        mean,
                        stdev
                ) { forger.aGaussianDouble(mean, stdev) }
            }

            it("forges mean when standard deviation is 0") {
                val mean = forger.aDouble(-1000.0, 1000.0)
                val stdev = 0.0

                repeat(testRepeatCountSmall) {
                    val result = forger.aGaussianDouble(mean, stdev)
                    assertThat(result).isEqualTo(mean)
                }
            }
        }

        context("forging constrained doubles") {

            it("forges a double …") {
                repeat(testRepeatCountSmall) {
                    val double = forger.aDouble(DoubleConstraint.ANY)
                    // How do you assert that an double is an double ... ?
                }
            }

            it("forges a positive double") {
                repeat(testRepeatCountSmall) {
                    val double = forger.aDouble(DoubleConstraint.POSITIVE)
                    assertThat(double)
                            .isGreaterThanOrEqualTo(0.0)
                }
            }

            it("forges a strictly positive double") {
                repeat(testRepeatCountSmall) {
                    val double = forger.aDouble(DoubleConstraint.POSITIVE_STRICT)
                    assertThat(double)
                            .isGreaterThan(0.0)
                }
            }

            it("forges a negative double") {
                repeat(testRepeatCountSmall) {
                    val double = forger.aDouble(DoubleConstraint.NEGATIVE)
                    assertThat(double)
                            .isLessThanOrEqualTo(0.0)
                }
            }

            it("forges a strictly negative double") {
                repeat(testRepeatCountSmall) {
                    val double = forger.aDouble(DoubleConstraint.NEGATIVE_STRICT)
                    assertThat(double)
                            .isLessThan(0.0)
                }
            }
        }

        // endregion

    }
})