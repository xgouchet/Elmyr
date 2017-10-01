package fr.xgouchet.elmyr

import org.assertj.core.api.Java6Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

/**
 * @author Xavier F. Gouchet
 */
class ForgerSpek_Collections : Spek({

    describe("A forger ") {
        val forger = Forger()
        var seed: Long = 0
        val testRepeatCountSmall = 16
        val arraySize = 32 + forger.aTinyInt()
        val arraySizeBig = 1024 + forger.aTinyInt()

        beforeEachTest {
            seed = System.nanoTime()
            forger.reset(seed)
        }


        context("forging elements from a collection") {
            it("selects a Float from a float array") {
                val data = FloatArray(arraySize) { (it * it * 3.14f) + (it * 1.618f) + 2.718f }

                repeat(testRepeatCountSmall, {
                    val result = forger.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                })
            }

            it("selects a Double from a double array") {
                val data = DoubleArray(arraySize) { (it * it * 3.14) + (it * 1.618) + 2.718 }

                repeat(testRepeatCountSmall, {
                    val result = forger.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                })
            }

            it("selects a Bool from a bool array") {
                val data = BooleanArray(arraySize) { forger.aBool() }

                repeat(testRepeatCountSmall, {
                    val result = forger.anElementFrom(data)
                    // How do you test a boolean ????
                })
            }

            it("selects a Long from a long array") {
                val data = LongArray(arraySize) { (it * it * 13L) + (it * 42L) + 69L }

                repeat(testRepeatCountSmall, {
                    val result = forger.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                })
            }

            it("selects an Int from an Int array") {
                val data = IntArray(arraySize) { (it * it * 13) + (it * 37) + 41 }

                repeat(testRepeatCountSmall, {
                    val result = forger.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                })
            }

            it("selects a Char from a Char array") {
                val data = CharArray(arraySize) { ((it * 13) + 21).toChar() }

                repeat(testRepeatCountSmall, {
                    val result = forger.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                })
            }

            it("selects an Object from a vararg") {
                val data = Array(arraySize) { "<$it> = ${Integer.toHexString(it)}" }

                repeat(testRepeatCountSmall, {
                    val result = forger.anElementFrom(*data)
                    assertThat(data)
                            .contains(result)
                })
            }

            it("selects an Entry from a map") {
                val data = HashMap<String, String>(arraySize)
                for (i in 0 until arraySize) {
                    data.put("$i-${forger.anInt()}", forger.aWord())
                }

                repeat(testRepeatCountSmall, {
                    val result = forger.anEntryFrom(data)
                    assertThat(data)
                            .contains(result)
                })
            }

            it("selects an Object from a Set") {
                val data = HashSet<String>(arraySize)
                for (i in 0 until arraySize) {
                    data.add(forger.aWord())
                }

                repeat(testRepeatCountSmall, {
                    val result = forger.anElementFrom(data)
                    assertThat(data)
                            .contains(result)
                })
            }
        }

        context("forging collections ") {

            it("forges an int array") {
                val data = forger.anIntArray(fr.xgouchet.elmyr.IntConstraint.SMALL, arraySize)

                assertThat(data)
                        .hasSize(arraySize)
                data.forEach { assertThat(it).isGreaterThan(0).isLessThan(fr.xgouchet.elmyr.Forger.Companion.SMALL_THRESHOLD) }
            }

            it("forges an int array with min/max") {
                val min = forger.aPositiveInt()
                val max = forger.anInt(min = min + 1)
                val data = forger.anIntArray(min, max, arraySize)

                assertThat(data)
                        .hasSize(arraySize)
                data.forEach { assertThat(it).isGreaterThanOrEqualTo(min).isLessThan(max) }
            }

            it("forges an int array with gaussian distribution") {

                forger.reset(seed)
                println("Seed = $seed")
                val mean = forger.aSmallInt()
                val stDev = forger.aTinyInt()
                val data = forger.anIntArrayWithDistribution(mean, stDev, arraySizeBig)

                assertThat(data)
                        .hasSize(arraySizeBig)

                verifyGaussianDistribution(arraySizeBig, mean.toDouble(), stDev.toDouble(), { i -> data[i].toDouble() })
            }

            it("forges an long array") {
                val data = forger.aLongArray(fr.xgouchet.elmyr.LongConstraint.NEGATIVE, arraySize)

                assertThat(data)
                        .hasSize(arraySize)
                data.forEach { assertThat(it).isLessThanOrEqualTo(0L) }
            }

            it("forges an long array with min/max") {
                val min = forger.aPositiveLong()
                val max = forger.aLong(min = min + 1)
                val data = forger.aLongArray(min, max, arraySize)

                assertThat(data)
                        .hasSize(arraySize)
                data.forEach { assertThat(it).isGreaterThanOrEqualTo(min).isLessThan(max) }
            }

            it("forges an long array with gaussian distribution") {

                forger.reset(seed)
                println("Seed = $seed")
                val size = 1024 + forger.aTinyInt()
                val mean = forger.aLong(1L, 512L)
                val stDev = forger.aLong(1L, 64L)
                val data = forger.aLongArrayWithDistribution(mean, stDev, size)

                assertThat(data)
                        .hasSize(size)

                verifyGaussianDistribution(size, mean.toDouble(), stDev.toDouble(), { i -> data[i].toDouble() })
            }

            it("forges a float array with constraint") {
                val constraint = forger.aValueFrom(FloatConstraint::class.java)
                val data = forger.aFloatArray(constraint, arraySize)

                assertThat(data)
                        .hasSize(arraySize)
            }

            it("forges a float array") {
                val min = forger.aPositiveFloat()
                val max = forger.aFloat(min = min + 1)
                val data = forger.aFloatArray(min, max, arraySize)

                assertThat(data)
                        .hasSize(arraySize)
                data.forEach { assertThat(it).isGreaterThanOrEqualTo(min).isLessThan(max) }
            }

            it("forges a float array with gaussian distribution") {

                forger.reset(seed)
                println("Seed = $seed")
                val size = 1024 + forger.aTinyInt()
                val mean = forger.aFloat(-1000f, 1000f)
                val stDev = forger.aFloat(10f, 500f)
                val data = forger.aFloatArrayWithDistribution(mean, stDev, size)

                assertThat(data)
                        .hasSize(size)

                verifyGaussianDistribution(size, mean.toDouble(), stDev.toDouble(), { i -> data[i].toDouble() })
            }

            it("forges a double array with constraint") {
                val constraint = forger.aValueFrom(DoubleConstraint::class.java)
                val data = forger.aDoubleArray(constraint, arraySize)

                assertThat(data)
                        .hasSize(arraySize)
            }

            it("forges a double array with min / max") {
                val min = forger.aPositiveDouble()
                val max = forger.aDouble(min = min + 1)
                val data = forger.aDoubleArray(min, max, arraySize)

                assertThat(data)
                        .hasSize(arraySize)
                data.forEach { assertThat(it).isGreaterThanOrEqualTo(min).isLessThan(max) }
            }

            it("forges a double array with gaussian distribution") {

                forger.reset(seed)
                println("Seed = $seed")
                val size = 1024 + forger.aTinyInt()
                val mean = forger.aDouble(-1000.0, 1000.0)
                val stDev = forger.aDouble(10.0, 500.0)
                val data = forger.aDoubleArrayWithDistribution(mean, stDev, size)

                assertThat(data)
                        .hasSize(size)

                verifyGaussianDistribution(size, mean, stDev, { i -> data[i] })
            }

            it("forges an char array") {
                val constraint = forger.aValueFrom(CharConstraint::class.java)
                val case = forger.aValueFrom(Case::class.java)
                val data = forger.aCharArray(constraint, case, arraySize)

                assertThat(data)
                        .hasSize(arraySize)
            }

            it("forges an char array with min/max") {
                val min = forger.aChar()
                val max = forger.aChar(min = min + 1)
                val data = forger.aCharArray(min, max, arraySize)

                assertThat(data)
                        .hasSize(arraySize)
                data.forEach { assertThat(it).isGreaterThanOrEqualTo(min).isLessThan(max) }
            }

            it("forges a String array with constraint") {
                val constraint = forger.aValueFrom(StringConstraint::class.java)
                val case = forger.aValueFrom(Case::class.java)
                val data = forger.aStringArray(constraint, case, arraySize)

                assertThat(data)
                        .hasSize(arraySize)
            }

            it("forges a String array with char constraint") {
                val constraint = forger.aValueFrom(CharConstraint::class.java)
                val case = forger.aValueFrom(Case::class.java)
                val data = forger.aStringArray(constraint, case, arraySize)

                assertThat(data)
                        .hasSize(arraySize)
            }

            it("forges a String array with regex") {
                val regex = "[\\d]+-[A-Z]+"
                val data = forger.aStringArray(regex, arraySize)

                assertThat(data)
                        .hasSize(arraySize)
                data.forEach { assertThat(it).matches(regex) }
            }

            it("forges a String array with regex") {
                val regex = Regex("""[\d]+-[A-Z]+""")
                val data = forger.aStringArray(regex, arraySize)

                assertThat(data)
                        .hasSize(arraySize)
                data.forEach { assertThat(it).matches(regex.pattern) }
            }
        }

        context("forging sublist") {

            it("forges a sublist of an empty list") {
                val inputList = emptyList<String>()
                val outputSize = forger.anInt(1, Forger.Companion.SMALL_THRESHOLD)

                val data = forger.aSubListOf(inputList, outputSize)

                assertThat(data)
                        .isNotNull()
                        .isEmpty()
            }

            it("forges a sublist of a non empty list, with enough data") {
                val inputSize = forger.anInt(forger.aSmallInt(), 1000)
                val inputList = ArrayList<String>(inputSize).apply{
                    for (i in 0 until inputSize) {
                        add("${i}_${forger.aWord()}")
                    }
                }

                val outputSize = forger.anInt(1, inputList.size)

                val data = forger.aSubListOf(inputList, outputSize)

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
                val inputSize = forger.anInt(forger.aSmallInt(), 1000)
                val inputList = ArrayList<String>(inputSize).apply{
                    for (i in 0 until inputSize) {
                        add("${i}_${forger.aWord()}")
                    }
                }

                val outputSize = inputSize

                val data = forger.aSubListOf(inputList, outputSize)

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

            it("forges a sublist of a non empty list, with not enough data") {
                val inputSize = forger.anInt(forger.aSmallInt(), 1000)
                val inputList = ArrayList<String>(inputSize).apply{
                    for (i in 0 until inputSize) {
                        add("${i}_${forger.aWord()}")
                    }
                }

                val outputSize = forger.anInt(inputSize + 1, 2000)

                val data : List<String> = forger.aSubListOf(inputList, outputSize)

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
    }

})