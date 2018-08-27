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
class ForgerSpek_Misc : Spek({

    describe("A forger ") {

        val forger = Forger()
        var seed: Long = 0
        val testRepeatCountSmall = 16
        val testRepeatCountHuge = 1024

        beforeEachTest {
            seed = System.nanoTime()
            forger.reset(seed)
        }

        context("forging nullable objects") {

            it("forges nullable value (with 50% probability)") {
                verifyProbability(testRepeatCountHuge, 0.5) {
                    val str = forger.aString()
                    val res = forger.aNullableFrom(str)
                    res == null
                }
            }

            it("forges nullable value with probability") {

                val probability = forger.aFloat(0f, 1f)
                verifyProbability(testRepeatCountHuge, probability.toDouble()) {
                    val str = forger.aString()
                    val res = forger.aNullableFrom(str, probability)
                    res == null
                }
            }
        }

        context("forging nullable objects with lamnbda") {

            it("forges nullable value (with 50% probability)") {
                verifyProbability(testRepeatCountHuge, 0.5) {
                    val res = forger.aNullableFrom { aString() }
                    res == null
                }
            }

            it("forges nullable value with probability") {

                val probability = forger.aFloat(0f, 1f)
                verifyProbability(testRepeatCountHuge, probability.toDouble()) {
                    val res = forger.aNullableFrom(probability) { aString() }
                    res == null
                }
            }
        }

        context("forging Dates") {

            it("forges a Date within a range") {
                repeat(testRepeatCountSmall) {
                    val range = forger.aLong(min = 1,
                            max = TimeUnit.DAYS.toMillis(10 * 365))
                    val date = forger.aDate(range)
                    val now = System.currentTimeMillis()

                    assertThat(Math.abs(date.time - now))
                            .isLessThanOrEqualTo(range)
                }
            }

            it("forges a Date within a range and big unit") {
                repeat(testRepeatCountSmall) {
                    val range = forger.aLong(min = 1, max = 24)
                    val date = forger.aDate(range, TimeUnit.HOURS)
                    val now = System.currentTimeMillis()
                    val rangeMs = range * 3600L * 1000L

                    assertThat(Math.abs(date.time - now))
                            .isLessThanOrEqualTo(rangeMs)
                }
            }

            it("forges a Date within a range and small unit") {
                repeat(testRepeatCountSmall) {
                    val range = forger.aPositiveLong(strict = true)
                    val date = forger.aDate(range, TimeUnit.NANOSECONDS)
                    val now = System.currentTimeMillis()
                    val rangeMs = range / 1000000L

                    assertThat(Math.abs(date.time - now))
                            .isLessThanOrEqualTo(rangeMs)
                }
            }

            it("forges a Future Date within a range") {
                repeat(testRepeatCountSmall) {
                    val range = forger.aLong(min = 1,
                            max = TimeUnit.DAYS.toMillis(10 * 365))
                    val now = System.currentTimeMillis()
                    val date = forger.aFuturDate(range)

                    assertThat(date.time - now)
                            .isLessThanOrEqualTo(range)
                            .isGreaterThan(0)
                }
            }

            it("forges a Future Date within a range and big unit") {
                repeat(testRepeatCountSmall) {
                    val range = forger.aLong(min = 1, max = 24)
                    val now = System.currentTimeMillis()
                    val date = forger.aFuturDate(range, TimeUnit.HOURS)
                    val rangeMs = range * 3600L * 1000L

                    assertThat(date.time - now)
                            .isLessThanOrEqualTo(rangeMs)
                            .isGreaterThan(0)
                }
            }

            it("forges a Future Date within a range and small unit") {
                repeat(testRepeatCountSmall) {
                    val range = forger.aPositiveLong(strict = true)
                    val now = System.currentTimeMillis()
                    val date = forger.aFuturDate(range, TimeUnit.NANOSECONDS)
                    val rangeMs = range / 1000000L

                    assertThat(date.time - now)
                            .isLessThanOrEqualTo(rangeMs)
                            .isGreaterThan(0)
                }
            }

            it("forges a Past Date within a range") {
                repeat(testRepeatCountSmall) {
                    val range = forger.aLong(min = 1,
                            max = TimeUnit.DAYS.toMillis(10 * 365))
                    val date = forger.aPastDate(range)
                    val now = System.currentTimeMillis()

                    assertThat(now - date.time)
                            .isLessThanOrEqualTo(range)
                            .isGreaterThan(0)
                }
            }

            it("forges a Past Date within a range and big unit") {
                repeat(testRepeatCountSmall) {
                    val range = forger.aLong(min = 1, max = 24)
                    val date = forger.aPastDate(range, TimeUnit.HOURS)
                    val now = System.currentTimeMillis()
                    val rangeMs = range * 3600L * 1000L

                    assertThat(now - date.time)
                            .isLessThanOrEqualTo(rangeMs)
                            .isGreaterThan(0)
                }
            }

            it("forges a Past Date within a range and small unit") {
                repeat(testRepeatCountSmall) {
                    val range = forger.aPositiveLong(strict = true)
                    val date = forger.aPastDate(range, TimeUnit.NANOSECONDS)
                    val now = System.currentTimeMillis()
                    val rangeMs = range / 1000000L

                    assertThat(now - date.time)
                            .isLessThanOrEqualTo(rangeMs)
                            .isGreaterThan(0)
                }
            }

            it("fails when range < 0") {
                repeat(testRepeatCountSmall) {
                    val range = -forger.aLong(min = 0,
                            max = TimeUnit.DAYS.toMillis(10 * 365))
                    throws<IllegalArgumentException> {
                        forger.aDate(range)
                    }
                }
            }
        }

    }
})
