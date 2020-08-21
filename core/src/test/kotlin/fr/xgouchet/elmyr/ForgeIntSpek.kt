package fr.xgouchet.elmyr

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ForgeIntSpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        val testRepeatCountSmall = 16

        beforeEachTest {
            seed = Forge.seed()
            forge.seed = seed
        }

        // region Integer in Range

        context("forging integers ") {

            it("fails if min > max") {
                val min = forge.aPositiveInt(strict = true)
                val max = forge.aNegativeInt(strict = true)
                throws<IllegalArgumentException> {
                    forge.anInt(min, max)
                }
            }

            it("fails if min == max") {
                val min = forge.anInt()
                throws<IllegalArgumentException> {
                    forge.anInt(min, min)
                }
            }

            it("forges an int in a specified range") {
                val min = forge.anInt()
                val max = forge.anInt(min = min + 1)

                repeat(testRepeatCountSmall) {
                    val int = forge.anInt(min, max)
                    assertThat(int)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                }
            }

            it("forges an int in minimal range") {
                val min = forge.anInt()
                val max = min + 1

                repeat(testRepeatCountSmall) {
                    val int = forge.anInt(min, max)
                    assertThat(int)
                            .isGreaterThanOrEqualTo(min)
                            .isLessThan(max)
                }
            }

            it("forges an int above a min") {
                val min = forge.anInt()

                repeat(testRepeatCountSmall) {
                    val int = forge.anInt(min = min)
                    assertThat(int)
                            .isGreaterThanOrEqualTo(min)
                }
            }

            it("forges an int below a max") {
                val max = forge.anInt()

                repeat(testRepeatCountSmall) {
                    val int = forge.anInt(max = max)
                    assertThat(int)
                            .isLessThan(max)
                }
            }
        }

        // endregion

        // region Integer with sign

        context("forging integers with sign") {

            it("forges a positive int") {
                repeat(testRepeatCountSmall) {
                    val int = forge.aPositiveInt(/*strict = false*/)
                    assertThat(int)
                            .isGreaterThanOrEqualTo(0)
                }
            }

            it("forges a strictly positive int") {
                repeat(testRepeatCountSmall) {
                    val int = forge.aPositiveInt(strict = true)
                    assertThat(int)
                            .isGreaterThan(0)
                }
            }

            it("forges a negative int") {
                repeat(testRepeatCountSmall) {
                    val int = forge.aNegativeInt(strict = false)
                    assertThat(int)
                            .isLessThanOrEqualTo(0)
                }
            }

            it("forges a strictly negative int") {
                repeat(testRepeatCountSmall) {
                    val int = forge.aNegativeInt(/*strict = true*/)
                    assertThat(int)
                            .isLessThan(0)
                }
            }
        }

        // endregion

        // region Integer in Gaussian distribution

        context("forging integers with gaussian") {

            it("fails if standard deviation < 0") {
                val mean = forge.aSmallInt()
                val stDev = forge.aNegativeInt(true)
                throws<IllegalArgumentException> {
                    forge.aGaussianInt(mean, stDev)
                }
            }

            it("fails if mean > MAX_VALUE/2") {
                val mean = forge.anInt(min = Int.MAX_VALUE / 2)
                val stDev = forge.anInt(1, 10)
                throws<IllegalArgumentException> {
                    forge.aGaussianInt(mean, stDev)
                }
            }

            it("fails if mean < -MAX_VALUE/2") {
                val mean = forge.anInt(max = -Int.MAX_VALUE / 2)
                val stDev = forge.anInt(1, 10)
                throws<IllegalArgumentException> {
                    forge.aGaussianInt(mean, stDev)
                }
            }

            it("forges a gaussian distributed int with defaults") {
                val mean = 0
                val stdev = 100

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianInt() }
            }

            it("forges a gaussian distributed int with defaults stdev") {
                val mean = forge.anInt(-1000, 1000)
                val stdev = 100

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianInt(mean) }
            }

            it("forges a gaussian distributed int with defaults mean") {
                val mean = 0
                val stdev = forge.anInt(10, 500)

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianInt(standardDeviation = stdev) }
            }

            it("forges a gaussian distributed int") {
                val mean = forge.anInt(-1000, 1000)
                val stdev = forge.anInt(10, 500)

                verifyGaussianDistribution(
                        mean,
                        stdev
                ) { forge.aGaussianInt(mean, stdev) }
            }

            it("forges mean when standard deviation is 0") {
                val mean = forge.anInt(-1000, 1000)
                val stdev = 0

                repeat(testRepeatCountSmall) {
                    val result = forge.aGaussianInt(mean, stdev)
                    assertThat(result).isEqualTo(mean)
                }
            }
        }

        // endregion

        // region Integer with "meaning"

        context("forging meaningful Integers") {

            it("forges a tiny int") {
                repeat(testRepeatCountSmall) {
                    val int = forge.aTinyInt()
                    assertThat(int)
                            .isGreaterThan(0)
                            .isLessThan(Forge.TINY_THRESHOLD)
                }
            }

            it("forges a small int") {
                repeat(testRepeatCountSmall) {
                    val int = forge.aSmallInt()
                    assertThat(int)
                            .isGreaterThan(0)
                            .isLessThan(Forge.SMALL_THRESHOLD)
                }
            }

            it("forges a big int") {
                repeat(testRepeatCountSmall) {
                    val int = forge.aBigInt()
                    assertThat(int)
                            .isGreaterThanOrEqualTo(Forge.BIG_THRESHOLD)
                }
            }

            it("forges a huge int") {
                repeat(testRepeatCountSmall) {
                    val int = forge.aHugeInt()
                    assertThat(int)
                            .isGreaterThanOrEqualTo(Forge.HUGE_THRESHOLD)
                }
            }
        }

        // endregion
    }
})
