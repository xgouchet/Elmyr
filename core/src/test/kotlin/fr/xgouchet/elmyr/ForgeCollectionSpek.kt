package fr.xgouchet.elmyr

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class ForgeCollectionSpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        val testRepeatCountSmall = 16
        val arraySize = 32 + forge.aTinyInt()
        val arraySizeBig = 1024 + forge.aSmallInt()

        beforeEachTest {
            seed = System.nanoTime()
            forge.seed = seed
        }

        // region Choosing from collection

        context("forging elements from a collection") {

            it("selects a Float from a float array") {
                val data = FloatArray(arraySize) { (it * it * 3.14f) + (it * 1.618f) + 2.718f }

                repeat(testRepeatCountSmall) {
                    val result = forge.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                }
            }

            it("selects a Double from a double array") {
                val data = DoubleArray(arraySize) { (it * it * 3.14) + (it * 1.618) + 2.718 }

                repeat(testRepeatCountSmall) {
                    val result = forge.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                }
            }

            it("selects a Bool from a bool array") {
                val data = BooleanArray(arraySize) { forge.aBool() }

                repeat(testRepeatCountSmall) {
                    val result = forge.anElementFrom(data)
                    // How do you test a boolean ????
                }
            }

            it("selects a Long from a long array") {
                val data = LongArray(arraySize) { (it * it * 13L) + (it * 42L) + 69L }

                repeat(testRepeatCountSmall) {
                    val result = forge.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                }
            }

            it("selects an Int from an Int array") {
                val data = IntArray(arraySize) { (it * it * 13) + (it * 37) + 41 }

                repeat(testRepeatCountSmall) {
                    val result = forge.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                }
            }

            it("selects a Char from a Char array") {
                val data = CharArray(arraySize) { ((it * 13) + 21).toChar() }

                repeat(testRepeatCountSmall) {
                    val result = forge.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                }
            }

            it("selects an Object from a vararg") {
                val data: Array<String> = Array(arraySize) { "<$it> = ${Integer.toHexString(it)}" }

                repeat(testRepeatCountSmall) {
                    val result = forge.anElementFrom(*data)
                    assertThat(data)
                            .contains(result)
                }
            }

            it("selects an Object from a list") {
                val data: List<String> = Array(arraySize) {
                    "<$it> = ${Integer.toHexString(it)}"
                }.toList()

                repeat(testRepeatCountSmall) {
                    val result = forge.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                }
            }

            it("selects an Entry from a map") {
                val data = HashMap<String, String>(arraySize)
                for (i in 0 until arraySize) {
                    data["$i-${forge.anInt()}"] = forge.aString()
                }

                repeat(testRepeatCountSmall) {
                    val result = forge.anEntryFrom(data)
                    assertThat(data)
                            .contains(result)
                }
            }

            it("selects an key from a map") {
                val data = HashMap<String, String>(arraySize)
                for (i in 0 until arraySize) {
                    data["$i-${forge.anInt()}"] = forge.aString()
                }

                repeat(testRepeatCountSmall) {
                    val result = forge.aKeyFrom(data)
                    assertThat(data)
                            .containsKey(result)
                }
            }

            it("selects an value from a map") {
                val data = HashMap<String, String>(arraySize)
                for (i in 0 until arraySize) {
                    data["$i-${forge.anInt()}"] = forge.aString()
                }

                repeat(testRepeatCountSmall) {
                    val result = forge.aValueFrom(data)
                    assertThat(data)
                            .containsValue(result)
                }
            }

            it("selects an Object from a Set") {
                val data = HashSet<String>(arraySize)
                for (i in 0 until arraySize) {
                    data.add(forge.aString())
                }

                repeat(testRepeatCountSmall) {
                    val result = forge.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                }
            }
        }
    }
})