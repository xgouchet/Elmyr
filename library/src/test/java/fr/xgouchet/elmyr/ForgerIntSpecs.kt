package fr.xgouchet.elmyr

import fr.xgouchet.elmyr.Forger
import fr.xgouchet.elmyr.Forger.Companion.BIG_THRESHOLD
import fr.xgouchet.elmyr.Forger.Companion.HUGE_THRESHOLD
import fr.xgouchet.elmyr.Forger.Companion.SMALL_THRESHOLD
import fr.xgouchet.elmyr.Forger.Companion.TINY_THRESHOLD
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Java6Assertions.assertThat

/**
 * @author Xavier F. Gouchet
 */
class ForgerIntSpecs : io.kotlintest.specs.FeatureSpec() {

    init {

        feature("An Number Forger uses a seed") {

            val forger = fr.xgouchet.elmyr.Forger()
            val seed = System.nanoTime()
            forger.reset(seed)
            val data = IntArray(16) { forger.anInt(min = it) }

            scenario("Reproduce data with another forger using the same seed") {
                val otherForger = fr.xgouchet.elmyr.Forger()
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
            val forger = fr.xgouchet.elmyr.Forger()
            forger.reset(System.nanoTime())

            scenario("Fail if min > max") {
                val min = forger.aPositiveInt(strict = true)
                val max = forger.aNegativeInt(strict = true)
                io.kotlintest.matchers.shouldThrow<IllegalArgumentException> {
                    forger.anInt(min, max)
                }
            }

            scenario("Fail if min == max") {
                val min = forger.anInt()
                io.kotlintest.matchers.shouldThrow<IllegalArgumentException> {
                    forger.anInt(min, min)
                }
            }
        }

        feature("An Number Forger produces meaningful ints") {

            val forger = fr.xgouchet.elmyr.Forger()
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
                            .isLessThan(fr.xgouchet.elmyr.Forger.Companion.TINY_THRESHOLD)
                })
            }

            scenario("Produces a small int") {
                repeat(16, {
                    val int = forger.aSmallInt()
                    assertThat(int)
                            .isGreaterThan(0)
                            .isLessThan(fr.xgouchet.elmyr.Forger.Companion.SMALL_THRESHOLD)
                })
            }

            scenario("Produces a big int") {
                repeat(16, {
                    val int = forger.aBigInt()
                    assertThat(int)
                            .isGreaterThanOrEqualTo(fr.xgouchet.elmyr.Forger.Companion.BIG_THRESHOLD)
                })
            }

            scenario("Produces a huge int") {
                repeat(16, {
                    val int = forger.aHugeInt()
                    assertThat(int)
                            .isGreaterThanOrEqualTo(fr.xgouchet.elmyr.Forger.Companion.HUGE_THRESHOLD)
                })
            }
        }


    }
}