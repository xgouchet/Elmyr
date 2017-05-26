package fr.xgouchet.elmyr

import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Assertions.assertThat
import org.mockito.AdditionalMatchers.or
import java.util.*

/**
 * @author Xavier F. Gouchet
 */
class ForgerCollectionsSpecs : FeatureSpec() {

    init {

        feature("A collection Forger uses a seed") {

            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)
            val data = IntArray(64) { (it * it * 13) + (it * 37) + 41 }

            val selected = forger.anElementFrom(data)
            assertThat(data).contains(selected)

            scenario("Reproduce data with another forger using the same seed") {
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

        feature("A collection Forger select from an array") {
            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)

            scenario("Select a Float from a float array") {
                val data = FloatArray(32) { (it * it * 3.14f) + (it * 1.618f) + 2.718f }

                repeat(64, {
                    val result = forger.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                })
            }

            scenario("Select a Double from a double array") {
                val data = DoubleArray(32) { (it * it * 3.14) + (it * 1.618) + 2.718 }

                repeat(64, {
                    val result = forger.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                })
            }

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
                    val result = forger.anElementFrom(*data)
                    assertThat(data)
                            .contains(result)
                })
            }

            scenario("Select an Object from a vararg") {
                val data0 = forger.aWord()
                val data1 = forger.aWord()
                val data2 = forger.aWord()
                val data3 = forger.aWord()
                val data4 = forger.aWord()
                val asArray = arrayOf(data0, data1, data2, data3, data4)

                repeat(64, {
                    val result = forger.anElementFrom(data0, data1, data2, data3, data4)
                    assertThat(asArray)
                            .contains(result)
                })
            }
        }

        feature("A collection Forger select from other collections") {
            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)

            scenario("Select an object from a set") {
                val data = Array(32) { "<$it> = ${Integer.toHexString(it)}" }
                val set = setOf(*data)

                repeat(64, {
                    val result = forger.anElementFrom(set)
                    assertThat(set)
                            .contains(result)
                })
            }

            scenario("Select an object from a List") {
                val data = Array(32) { "<$it> = ${Integer.toHexString(it)}" }
                val list = listOf(*data)

                repeat(64, {
                    val result = forger.anElementFrom(list)
                    assertThat(list)
                            .contains(result)
                })
            }

            scenario("Select a key / value pair from a map") {
                val data = Array(32) { Pair<Int, String>(it, Integer.toHexString(it)) }
                val map = mapOf(*data)

                repeat(64, {
                    val result = forger.anElementFrom(map)
                    assertThat(map)
                            .containsEntry(result.key, result.value)
                })
            }
        }
    }
}