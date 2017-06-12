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
        val arraySize = 32

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
                val size = forger.aTinyInt()
                val data = forger.anIntArray(fr.xgouchet.elmyr.IntConstraint.SMALL, size)

                assertThat(data)
                        .hasSize(size)
                data.forEach { assertThat(it).isGreaterThan(0).isLessThan(fr.xgouchet.elmyr.Forger.Companion.SMALL_THRESHOLD) }
            }

            it("forges an int array with min/max") {
                val size = forger.aTinyInt()
                val min = forger.aPositiveInt()
                val max = forger.anInt(min = min + 1)
                val data = forger.anIntArray(min, max, size)

                assertThat(data)
                        .hasSize(size)
                data.forEach { assertThat(it).isGreaterThanOrEqualTo(min).isLessThan(max) }
            }

            it("forges an int array with gaussian distribution") {

                forger.reset(seed)
                println("Seed = $seed")
                val size = 1024 + forger.aTinyInt()
                val mean = forger.aSmallInt()
                val stDev = forger.aTinyInt()
                val data = forger.anIntArrayWithDistribution(mean, stDev, size)

                assertThat(data)
                        .hasSize(size)

                verifyGaussianDistribution(size, mean.toDouble(), stDev.toDouble(), { i -> data[i].toDouble() })
            }

            it("forges an long array") {
                val size = forger.aTinyInt()
                val data = forger.aLongArray(fr.xgouchet.elmyr.LongConstraint.NEGATIVE, size)

                assertThat(data)
                        .hasSize(size)
                data.forEach { assertThat(it).isLessThanOrEqualTo(0L) }
            }

            it("forges an long array with min/max") {
                val size = forger.aTinyInt()
                val min = forger.aPositiveLong()
                val max = forger.aLong(min = min + 1)
                val data = forger.aLongArray(min, max, size)

                assertThat(data)
                        .hasSize(size)
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
                val size = forger.aTinyInt()
                val constraint = forger.aValueFrom(FloatConstraint::class.java)
                val data = forger.aFloatArray(constraint, size)

                assertThat(data)
                        .hasSize(size)
            }

            it("forges a float array") {
                val size = forger.aTinyInt()
                val min = forger.aPositiveFloat()
                val max = forger.aFloat(min = min + 1)
                val data = forger.aFloatArray(min, max, size)

                assertThat(data)
                        .hasSize(size)
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
                val size = forger.aTinyInt()
                val constraint = forger.aValueFrom(DoubleConstraint::class.java)
                val data = forger.aDoubleArray(constraint, size)

                assertThat(data)
                        .hasSize(size)
            }

            it("forges a double array with min / max") {
                val size = forger.aTinyInt()
                val min = forger.aPositiveDouble()
                val max = forger.aDouble(min = min + 1)
                val data = forger.aDoubleArray(min, max, size)

                assertThat(data)
                        .hasSize(size)
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
                val size = forger.aTinyInt()
                val constraint = forger.aValueFrom(CharConstraint::class.java)
                val data = forger.aCharArray(constraint, Case.LOWER, size)

                assertThat(data)
                        .hasSize(size)
            }

            it("forges an char array with min/max") {
                val size = forger.aTinyInt()
                val min = forger.aChar()
                val max = forger.aChar(min = min + 1)
                val data = forger.aCharArray(min, max, size)

                assertThat(data)
                        .hasSize(size)
                data.forEach { assertThat(it).isGreaterThanOrEqualTo(min).isLessThan(max) }
            }
        }

    }

})