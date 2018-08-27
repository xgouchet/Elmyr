package fr.xgouchet.elmyr

import org.assertj.core.api.Java6Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

/**
 * @author Xavier F. Gouchet
 */
class ForgerSpek_Reproducibility : Spek({

    describe("A forger ") {
        val forger = Forger()
        var seed: Long = 0
        var data: IntArray = IntArray(0)
        val arraySize = 32

        beforeEachTest {
            seed = System.nanoTime()
            forger.reset(seed)
            data = IntArray(arraySize) { forger.anInt(min = it) }
        }

        context("with reproducibility ") {

            it("reproduces data with another forger using the same seed") {
                val otherForger = Forger()
                otherForger.reset(seed)

                val otherData = IntArray(arraySize) { otherForger.anInt(min = it) }

                assertThat(otherData).containsExactly(*data)
            }

            it("reproduces data with same forger reset with the same seed") {
                forger.reset(seed)

                val otherData = IntArray(arraySize) { forger.anInt(min = it) }

                assertThat(otherData)
                        .containsExactly(*data)
            }

            it("forges different data with different seed") {
                val otherSeed = System.nanoTime()
                forger.reset(otherSeed)
                val otherData = IntArray(arraySize) { forger.anInt(min = it) }

                assertThat(otherData)
                        .isNotEqualTo(data)
            }

            it("keeps track of the current seed") {
                val actualSeed = forger.seed

                assertThat(actualSeed).isEqualTo(seed)
            }
        }

    }
})