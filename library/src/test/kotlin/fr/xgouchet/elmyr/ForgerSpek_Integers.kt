package fr.xgouchet.elmyr

import org.assertj.core.api.Java6Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.util.concurrent.TimeUnit

/**
 * @author Xavier F. Gouchet
 */
class ForgerSpek_Integers : Spek({

    describe("A forger ") {
        val forger = Forger()
        var seed: Long = 0
        val testRepeatCountSmall = 16
        val testRepeatCountHuge = 1024

        beforeEachTest {
            seed = System.nanoTime()
            forger.reset(seed)
        }

        context("forging booleans ") {

            it("forges random booleans (with 50% probability)") {
                verifyProbability(testRepeatCountHuge, 0.5) { forger.aBool() }
            }

            it("forges random boolean with probability") {
                val probability = forger.aFloat(0f, 1f)
                verifyProbability(testRepeatCountHuge, probability.toDouble()) { forger.aBool(probability) }
            }
        }

        // region Integers

        context("forging integers ") {

            it("fails if min > max") {
                val min = forger.aPositiveInt(strict = true)
                val max = forger.aNegativeInt(strict = true)
                throws<IllegalArgumentException> {
                    forger.anInt(min, max)
                }
            }

            it("fails if min == max") {
                val min = forger.anInt()
                throws<IllegalArgumentException> {
                    forger.anInt(min, min)
                }
            }

            it("fails if standard deviation < 0") {
                val mean = forger.aSmallInt()
                val stDev = forger.aNegativeInt(true)
                throws<IllegalArgumentException> {
                    forger.aGaussianInt(mean, stDev)
                }
            }

            it("fails if mean > MAX_VALUE/2") {
                val mean = forger.anInt(min = Int.MAX_VALUE / 2)
                val stDev = forger.anInt(1, 10)
                throws<IllegalArgumentException> {
                    forger.aGaussianInt(mean, stDev)
                }
            }

            it("fails if mean < -MAX_VALUE/2") {
                val mean = forger.anInt(max = -Int.MAX_VALUE / 2)
                val stDev = forger.anInt(1, 10)
                throws<IllegalArgumentException> {
                    forger.aGaussianInt(mean, stDev)
                }
            }

            it("forges an int in a specified range") {
                val min = forger.anInt()
                val max = forger.anInt(min + 1)

                assertThat(max).isGreaterThan(min)

                repeat(testRepeatCountSmall) {
                    val int = forger.anInt(min, max)
                    assertThat(int)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                }
            }

            it("forges an int in a small range") {
                val min = forger.anInt()
                val max = min + 3

                repeat(testRepeatCountSmall) {
                    val int = forger.anInt(min, max)
                    assertThat(int)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                }
            }

            it("forges a positive int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.aPositiveInt(strict = false)
                    assertThat(int)
                            .isGreaterThanOrEqualTo(0)
                }
            }

            it("forges a strictly positive int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.aPositiveInt(strict = true)
                    assertThat(int)
                            .isGreaterThan(0)
                }
            }

            it("forges a negative int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.aNegativeInt(strict = false)
                    assertThat(int)
                            .isLessThanOrEqualTo(0)
                }
            }

            it("forges a strictly negative int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.aNegativeInt(strict = true)
                    assertThat(int)
                            .isLessThan(0)
                }
            }

            it("forges a gaussian distributed int") {
                val mean = forger.anInt(-1000, 1000)
                val stdev = forger.anInt(10, 500)

                verifyGaussianDistribution(
                        testRepeatCountHuge,
                        mean.toDouble(),
                        stdev.toDouble()
                ) { forger.aGaussianInt(mean, stdev).toDouble() }
            }

            it("forges mean when standard deviation is 0") {
                val mean = forger.anInt(-1000, 1000)
                val stdev = 0

                repeat(testRepeatCountSmall) {
                    val result = forger.aGaussianInt(mean, stdev)
                    assertThat(result).isEqualTo(mean)
                }
            }
        }

        context("forging constrained integers") {

            it("forges an int …") {
                repeat(testRepeatCountSmall) {
                    val int = forger.anInt(IntConstraint.ANY)
                    // How do you assert that an int is an int ... ?
                }
            }

            it("forges a positive int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.anInt(IntConstraint.POSITIVE)
                    assertThat(int)
                            .isGreaterThanOrEqualTo(0)
                }
            }

            it("forges a strictly positive int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.anInt(IntConstraint.POSITIVE_STRICT)
                    assertThat(int)
                            .isGreaterThan(0)
                }
            }

            it("forges a negative int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.anInt(IntConstraint.NEGATIVE)
                    assertThat(int)
                            .isLessThanOrEqualTo(0)
                }
            }

            it("forges a strictly negative int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.anInt(IntConstraint.NEGATIVE_STRICT)
                    assertThat(int)
                            .isLessThan(0)
                }
            }

            it("forges a tiny int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.anInt(IntConstraint.TINY)
                    assertThat(int)
                            .isGreaterThan(0)
                            .isLessThan(Forger.TINY_THRESHOLD)
                }
            }

            it("forges a small int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.anInt(IntConstraint.SMALL)
                    assertThat(int)
                            .isGreaterThan(0)
                            .isLessThan(Forger.SMALL_THRESHOLD)
                }
            }

            it("forges a big int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.anInt(IntConstraint.BIG)
                    assertThat(int)
                            .isGreaterThanOrEqualTo(Forger.BIG_THRESHOLD)
                }
            }

            it("forges a huge int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.anInt(IntConstraint.HUGE)
                    assertThat(int)
                            .isGreaterThanOrEqualTo(Forger.HUGE_THRESHOLD)
                }
            }

        }

        context("forging meaningful integers ") {

            it("forges a tiny int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.aTinyInt()
                    assertThat(int)
                            .isGreaterThan(0)
                            .isLessThan(Forger.TINY_THRESHOLD)
                }
            }

            it("forges a small int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.aSmallInt()
                    assertThat(int)
                            .isGreaterThan(0)
                            .isLessThan(Forger.SMALL_THRESHOLD)
                }
            }

            it("forges a big int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.aBigInt()
                    assertThat(int)
                            .isGreaterThanOrEqualTo(Forger.BIG_THRESHOLD)
                }
            }

            it("forges a huge int") {
                repeat(testRepeatCountSmall) {
                    val int = forger.aHugeInt()
                    assertThat(int)
                            .isGreaterThanOrEqualTo(Forger.HUGE_THRESHOLD)
                }
            }

        }

        // endregion

        // region Longs

        context("forging longs") {

            it("fails if min > max") {
                val min = forger.aPositiveLong(strict = true)
                val max = forger.aNegativeLong(strict = true)
                throws<IllegalArgumentException> {
                    forger.aLong(min, max)
                }
            }

            it("fails if min == max") {
                val min = forger.aLong()
                throws<IllegalArgumentException> {
                    forger.aLong(min, min)
                }
            }

            it("fails if standard deviation < 0") {
                val mean = forger.aLong(1, Forger.SMALL_THRESHOLD.toLong())
                val stDev = forger.aNegativeLong(true)
                throws<IllegalArgumentException> {
                    forger.aGaussianLong(mean, stDev)
                }
            }

            it("fails if mean > MAX_VALUE/2") {
                val mean = forger.aLong(min = Long.MAX_VALUE / 2)
                val stDev = forger.aLong(1, 10)
                throws<IllegalArgumentException> {
                    forger.aGaussianLong(mean, stDev)
                }
            }

            it("fails if mean < -MAX_VALUE/2") {
                val mean = forger.aLong(max = -Long.MAX_VALUE / 2)
                val stDev = forger.aLong(1, 10)
                throws<IllegalArgumentException> {
                    forger.aGaussianLong(mean, stDev)
                }
            }

            it("forges an long in a specified range") {
                val min = forger.aLong()
                val max = forger.aLong(min + 1)

                assertThat(max).isGreaterThan(min)

                repeat(16) {
                    val long = forger.aLong(min, max)
                    assertThat(long)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                }
            }

            it("forges an long in a small range") {
                val min = forger.aLong()
                val max = min + 3

                repeat(16) {
                    val long = forger.aLong(min, max)
                    assertThat(long)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                }
            }

            it("forges a positive long") {
                repeat(16) {
                    val long = forger.aPositiveLong(strict = false)
                    assertThat(long)
                            .isGreaterThanOrEqualTo(0)
                }
            }

            it("forges a strictly positive long") {
                repeat(16) {
                    val long = forger.aPositiveLong(strict = true)
                    assertThat(long)
                            .isGreaterThan(0)
                }
            }

            it("forges a negative long") {
                repeat(16) {
                    val long = forger.aNegativeLong(strict = false)
                    assertThat(long)
                            .isLessThanOrEqualTo(0)
                }
            }

            it("forges a strictly negative long") {
                repeat(16) {
                    val long = forger.aNegativeLong(strict = true)
                    assertThat(long)
                            .isLessThan(0)
                }
            }

            it("forges a gaussian distributed long") {
                val mean = forger.aLong(-1000, 1000)
                val stdev = forger.aLong(10, 500)

                verifyGaussianDistribution(
                        testRepeatCountHuge,
                        mean.toDouble(),
                        stdev.toDouble()
                ) { forger.aGaussianLong(mean, stdev).toDouble() }
            }

            it("forges mean when standard deviation is 0") {
                val mean = forger.aLong(-1000L, 1000L)
                val stdev = 0L

                repeat(testRepeatCountSmall) {
                    val result = forger.aGaussianLong(mean, stdev)
                    assertThat(result).isEqualTo(mean)
                }
            }
        }

        context("forging constrained longs") {

            it("forges a long …") {
                repeat(testRepeatCountSmall) {
                    val long = forger.aLong(LongConstraint.ANY)
                    // How do you assert that an long is an long ... ?
                }
            }

            it("forges a positive long") {
                repeat(testRepeatCountSmall) {
                    val long = forger.aLong(LongConstraint.POSITIVE)
                    assertThat(long)
                            .isGreaterThanOrEqualTo(0)
                }
            }

            it("forges a strictly positive long") {
                repeat(testRepeatCountSmall) {
                    val long = forger.aLong(LongConstraint.POSITIVE_STRICT)
                    assertThat(long)
                            .isGreaterThan(0)
                }
            }

            it("forges a negative long") {
                repeat(testRepeatCountSmall) {
                    val long = forger.aLong(LongConstraint.NEGATIVE)
                    assertThat(long)
                            .isLessThanOrEqualTo(0)
                }
            }

            it("forges a strictly negative long") {
                repeat(testRepeatCountSmall) {
                    val long = forger.aLong(LongConstraint.NEGATIVE_STRICT)
                    assertThat(long)
                            .isLessThan(0)
                }
            }
        }

        context("forging timestamps") {

            it("forges a timestamp within a range") {
                repeat(testRepeatCountSmall) {
                    val range = forger.aLong(min = 1,
                            max = TimeUnit.DAYS.toMillis(10 * 365))
                    val timestamp = forger.aTimestamp(range)
                    val now = System.currentTimeMillis()
                    System.nanoTime()
                    assertThat(Math.abs(timestamp - now))
                            .isLessThanOrEqualTo(range)
                }
            }

            it("forges a timestamp within a range and big unit") {
                repeat(testRepeatCountSmall) {
                    val range = forger.aLong(min = 1, max = 24)
                    val timestamp = forger.aTimestamp(range, TimeUnit.HOURS)
                    val now = System.currentTimeMillis()
                    val rangeMs = range * 3600L * 1000L

                    System.nanoTime()
                    assertThat(Math.abs(timestamp - now))
                            .isLessThanOrEqualTo(rangeMs)
                }
            }

            it("forges a timestamp within a range and small unit") {
                repeat(testRepeatCountSmall) {
                    val range = forger.aPositiveLong(strict = true)
                    val timestamp = forger.aTimestamp(range, TimeUnit.NANOSECONDS)
                    val now = System.currentTimeMillis()
                    val rangeMs = range / 1000000L

                    System.nanoTime()
                    assertThat(Math.abs(timestamp - now))
                            .isLessThanOrEqualTo(rangeMs)
                }
            }

            it("fails when range < 0") {
                repeat(testRepeatCountSmall) {
                    val range = -forger.aLong(min = 0,
                            max = TimeUnit.DAYS.toMillis(10 * 365))
                    throws<IllegalArgumentException> {
                        forger.aTimestamp(range)
                    }
                }
            }
        }

        // endregion

    }
})