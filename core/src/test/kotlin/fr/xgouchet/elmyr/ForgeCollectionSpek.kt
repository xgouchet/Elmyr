package fr.xgouchet.elmyr

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.assertj.core.api.KotlinAssertions.assertThat as assertThatK

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

        // endregion

        // region Transforming collections

        context("forging sublists") {

            it("forges a sublist of an empty list") {
                val inputList = emptyList<String>()
                val outputSize = forge.anInt(1, Forge.SMALL_THRESHOLD)

                val data = forge.aSubListOf(inputList, outputSize)

                assertThat(data)
                        .isNotNull()
                        .isEmpty()
            }

            it("forges an empty sublist of a non empty list") {
                val inputSize = forge.anInt(forge.aSmallInt(), 1000)
                val inputList = ArrayList<String>(inputSize).apply {
                    for (i in 0 until inputSize) {
                        add("${i}_${forge.aString()}")
                    }
                }
                val outputSize = 0

                val data = forge.aSubListOf(inputList, outputSize)

                assertThat(data)
                        .isNotNull()
                        .isEmpty()
            }

            it("forges a sublist of a non empty list, with random size") {
                val inputSize = forge.anInt(forge.aSmallInt(), 1000)
                val inputList = ArrayList<String>(inputSize).apply {
                    for (i in 0 until inputSize) {
                        add("${i}_${forge.aString()}")
                    }
                }

                val data = forge.aSubListOf(inputList)

                assertThat(data)
                        // output is a new list, non null
                        .isNotNull()
                        .isNotSameAs(inputList)
                        // no duplicates, no null
                        .doesNotHaveDuplicates()
                        .doesNotContainNull()
            }

            it("forges a sublist of a non empty list, with enough data") {
                val inputSize = forge.anInt(forge.aSmallInt(), 1000)
                val inputList = ArrayList<String>(inputSize).apply {
                    for (i in 0 until inputSize) {
                        add("${i}_${forge.aString()}")
                    }
                }

                val outputSize = forge.anInt(1, inputList.size)

                val data = forge.aSubListOf(inputList, outputSize)

                assertThat(data)
                        // output is a new list, non null
                        .isNotNull()
                        .isNotSameAs(inputList)
                        // check the size
                        .hasSize(outputSize)
                        // no duplicates, no null
                        .doesNotHaveDuplicates()
                        .doesNotContainNull()
            }

            it("forges a sublist of a non empty list, with exactly enough data") {
                val inputSize = forge.anInt(forge.aSmallInt(), 1000)
                val inputList = ArrayList<String>(inputSize).apply {
                    for (i in 0 until inputSize) {
                        add("${i}_${forge.aString()}")
                    }
                }

                val data = forge.aSubListOf(inputList, inputSize)

                assertThat(data)
                        // output is a new list, non null
                        .isNotNull()
                        .isNotSameAs(inputList)
                        // check the size
                        .hasSize(inputSize)
                        // no duplicates, no null
                        .doesNotHaveDuplicates()
                        .doesNotContainNull()
            }

            it("forges a sublist of a non empty list, with one item more than needed") {
                val inputSize = forge.anInt(forge.aSmallInt(), 1000)
                val inputList = ArrayList<String>(inputSize).apply {
                    for (i in 0 until inputSize) {
                        add("${i}_${forge.aString()}")
                    }
                }

                val data = forge.aSubListOf(inputList, inputSize - 1)

                assertThat(data)
                        // output is a new list, non null
                        .isNotNull()
                        .isNotSameAs(inputList)
                        // check the size
                        .hasSize(inputSize - 1)
                        // no duplicates, no null
                        .doesNotHaveDuplicates()
                        .doesNotContainNull()
            }

            it("forges a sublist of a non empty list, with not enough data") {
                val inputSize = forge.anInt(forge.aSmallInt(), 1000)
                val inputList = ArrayList<String>(inputSize).apply {
                    for (i in 0 until inputSize) {
                        add("${i}_${forge.aString()}")
                    }
                }

                val outputSize = forge.anInt(inputSize + 1, 2000)

                val data: List<String> = forge.aSubListOf(inputList, outputSize)

                assertThat(data)
                        // output is a new list, non null
                        .isNotNull()
                        .isNotSameAs(inputList)
                        // check the size
                        .hasSize(inputSize)
                        // no duplicates, no null
                        .doesNotHaveDuplicates()
                        .doesNotContainNull()
            }
        }

        context("forging subset") {

            it("forges a subset of an empty set") {
                val inputSet = emptySet<String>()
                val outputSize = forge.anInt(1, Forge.SMALL_THRESHOLD)

                val data = forge.aSubSetOf(inputSet, outputSize)

                assertThat(data)
                        .isNotNull()
                        .isEmpty()
            }

            it("forges an empty subset of a non empty set") {
                val inputSize = forge.anInt(forge.aSmallInt(), 1000)
                val inputSet = HashSet<String>(inputSize).apply {
                    for (i in 0 until inputSize) {
                        add("${i}_${forge.aString()}")
                    }
                }
                val outputSize = 0

                val data = forge.aSubSetOf(inputSet, outputSize)

                assertThat(data)
                        .isNotNull()
                        .isEmpty()
            }

            it("forges a subset of a non empty set, with random size") {
                val inputSize = forge.anInt(forge.aSmallInt(), 1000)
                val inputSet = HashSet<String>(inputSize).apply {
                    for (i in 0 until inputSize) {
                        add("${i}_${forge.aString()}")
                    }
                }

                val data = forge.aSubSetOf(inputSet)

                assertThat(data)
                        // output is a new set, non null
                        .isNotNull()
                        .isNotSameAs(inputSet)
                        // no duplicates, no null
                        .doesNotHaveDuplicates()
                        .doesNotContainNull()
            }

            it("forges a subset of a non empty set, with enough data") {
                val inputSize = forge.anInt(forge.aSmallInt(), 1000)
                val inputSet = HashSet<String>(inputSize).apply {
                    for (i in 0 until inputSize) {
                        add("${i}_${forge.aString()}")
                    }
                }

                val outputSize = forge.anInt(1, inputSet.size)

                val data = forge.aSubSetOf(inputSet, outputSize)

                assertThat(data)
                        // output is a new set, non null
                        .isNotNull()
                        .isNotSameAs(inputSet)
                        // check the size
                        .hasSize(outputSize)
                        // no duplicates, no null
                        .doesNotHaveDuplicates()
                        .doesNotContainNull()
            }

            it("forges a subset of a non empty set, with exactly enough data") {
                val inputSize = forge.anInt(forge.aSmallInt(), 1000)
                val inputSet = HashSet<String>(inputSize).apply {
                    for (i in 0 until inputSize) {
                        add("${i}_${forge.aString()}")
                    }
                }

                val data = forge.aSubSetOf(inputSet, inputSize)

                assertThat(data)
                        // output is a new set, non null
                        .isNotNull()
                        .isNotSameAs(inputSet)
                        // check the size
                        .hasSize(inputSize)
                        // no duplicates, no null
                        .doesNotHaveDuplicates()
                        .doesNotContainNull()
            }

            it("forges a subset of a non empty set, with one more item than needed") {
                val inputSize = forge.anInt(forge.aSmallInt(), 1000)
                val inputSet = HashSet<String>(inputSize).apply {
                    for (i in 0 until inputSize) {
                        add("${i}_${forge.aString()}")
                    }
                }

                val data = forge.aSubSetOf(inputSet, inputSize - 1)

                assertThat(data)
                        // output is a new set, non null
                        .isNotNull()
                        .isNotSameAs(inputSet)
                        // check the size
                        .hasSize(inputSize - 1)
                        // no duplicates, no null
                        .doesNotHaveDuplicates()
                        .doesNotContainNull()
            }

            it("forges a subset of a non empty set, with not enough data") {
                val inputSize = forge.anInt(forge.aSmallInt(), 1000)
                val inputSet = HashSet<String>(inputSize).apply {
                    for (i in 0 until inputSize) {
                        add("${i}_${forge.aString()}")
                    }
                }

                val outputSize = forge.anInt(inputSize + 1, 2000)

                val data: Set<String> = forge.aSubSetOf(inputSet, outputSize)

                assertThat(data)
                        // output is a new set, non null
                        .isNotNull()
                        .isNotSameAs(inputSet)
                        // check the size
                        .hasSize(inputSize)
                        // no duplicates, no null
                        .doesNotHaveDuplicates()
                        .doesNotContainNull()
            }
        }

        context("shuffling a list") {

            it("shuffles an empty list") {
                val inputList = emptyList<String>()

                val data = forge.shuffle(inputList)

                assertThat(data)
                        .isNotNull()
                        .isEmpty()
            }

            it("shuffles a singleton list") {
                val inputList = listOf(forge.aString())

                val data = forge.shuffle(inputList)

                assertThat(data)
                        .isNotNull()
                        .containsAll(inputList)
            }

            it("shuffles a non empty list") {
                val size = forge.aSmallInt() + 128
                val inputList = Array(size) { forge.aString() }.toList()
                var previousList: List<String>? = null

                repeat(testRepeatCountSmall) { _ ->
                    val shuffled = forge.shuffle(inputList)

                    assertThatK(shuffled)
                            .isNotSameAs(inputList)
                            .isNotNull()
                            .containsAll(inputList)

                    previousList?.let {
                        var sameOrder = true
                        for (i in 0 until size) {
                            if (shuffled[i] != it[i]) {
                                sameOrder = false
                                break
                            }
                        }
                        assertThat(sameOrder).isFalse()
                    }

                    previousList = shuffled
                }
            }
        }

        // endregion

        // region Generate a list

        context("forging collections ") {

            it("forges a list of whatever") {
                var i = 0
                val data = forge.aList { anElementFrom(i++) }
                val expectedData = IntArray(data.size) { it }.toTypedArray()

                assertThat(data).containsExactly(*expectedData)
            }

            it("forges a list of whatever with fixed size") {
                val size = forge.aTinyInt() + 3
                var i = 0
                val data = forge.aList(size) { anElementFrom(i++) }
                val expectedData = IntArray(size) { it }.toTypedArray()

                assertThat(data).containsExactly(*expectedData)
            }

            it("forges a map of whatever") {
                var i = 0
                val data = forge.aMap() { i to "<${i++}>" }
                val expectedData = mutableMapOf<Int, String>()
                for (j in 0 until data.size) {
                    expectedData[j] = "<$j>"
                }

                assertThat(data)
                        .contains(*expectedData.entries.toTypedArray())
            }

            it("forges a map of whatever with fixed size") {
                val size = forge.aTinyInt() + 3
                var i = 0
                val values = forge.aList(size) { aString() }
                val data = forge.aMap(size) { i to values[i++] }
                val expectedData = mutableMapOf<Int, String>()
                for (j in 0 until size) {
                    expectedData[j] = values[j]
                }

                assertThat(data)
                        .contains(*expectedData.entries.toTypedArray())
            }
        }
        // endregion
    }
})