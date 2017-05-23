package fr.xgouchet.elmyr

import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Assertions.assertThat

/**
 * @author Xavier F. Gouchet
 */
class ForgerArraySpecs : FeatureSpec() {

    init {

        feature("An Array Forger uses a seed") {

            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)
            val data = IntArray(64) { (it * it * 13) + (it * 37) + 41 }

            val selected = forger.anElementFrom(data)
            assertThat(data).contains(selected)

            scenario("Reproduce data with another FORGER using the same seed") {
                val otherForger = Forger()
                otherForger.reset(seed)
                val otherSelected = otherForger.anElementFrom(data)

                assertThat(otherSelected)
                        .isEqualTo(selected)
            }

            scenario("Reproduce data with same FORGER reset with the same seed") {
                forger.reset(seed)
                val otherSelected = forger.anElementFrom(data)

                assertThat(otherSelected)
                        .isEqualTo(selected)
            }


            scenario("Produce different data with different seed") {
                val otherSeed = System.nanoTime()
                forger.reset(otherSeed)
                val otherSelected = forger.anElementFrom(data)

                assertThat(data)
                        .contains(otherSelected)
                assertThat(otherSelected)
                        .isNotEqualTo(selected)

            }
        }

        feature("An Array Forger select from an array") {
            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)

            scenario("Select an Int from an Int array") {
                val data = IntArray(32) { (it * it * 13) + (it * 37) + 41 }

                repeat(64, {
                    val result = forger.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                })
            }

            scenario("Select a Char from a Char array") {
                val data = CharArray(32) { ((it * 13) + 21).toChar() }

                repeat(64, {
                    val result = forger.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                })
            }

            scenario("Select an Object from an object array") {
                val data = Array(32) { "<$it> = ${Integer.toHexString(it)}" }

                repeat(64, {
                    val result = forger.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                })
            }
        }
    }
}