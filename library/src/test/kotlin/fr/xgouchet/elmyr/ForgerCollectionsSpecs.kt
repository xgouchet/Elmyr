package fr.xgouchet.elmyr

import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Java6Assertions.within
import java.lang.Math.abs
import java.lang.Math.sqrt

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

        feature("A collection Forger forges primitive arrays") {
            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)

            scenario("Forge an int array") {
                val size = forger.aTinyInt()
                val data = forger.anIntArray(IntConstraint.SMALL, size)

                assertThat(data)
                        .hasSize(size)
                data.forEach { assertThat(it).isGreaterThan(0).isLessThan(Forger.SMALL_THRESHOLD) }
            }

            scenario("Forge an int array with min/max") {
                val size = forger.aTinyInt()
                val min = forger.aPositiveInt()
                val max = forger.anInt(min = min + 1)
                val data = forger.anIntArray(min, max, size)

                assertThat(data)
                        .hasSize(size)
                data.forEach { assertThat(it).isGreaterThanOrEqualTo(min).isLessThan(max) }
            }

            scenario("Forge an int array with gaussian distribution") {

                forger.reset(seed)
                println("Seed = $seed")
                val size = 1024 + forger.aTinyInt()
                val mean = forger.aSmallInt()
                val stDev = forger.aTinyInt()
                val data = forger.anIntArrayWithDistribution(mean, stDev, size)

                assertThat(data)
                        .hasSize(size)

                var sum = 0f
                var squareSum = 0f
                data.forEach {
                    sum += it
                    squareSum += (it * it)
                }

                val computedMean = sum / size
                val computedStDev = sqrt(abs(squareSum - (size * mean * mean)) / (size - 1.0)).toFloat()

                assertThat(computedMean)
                        .isCloseTo(mean.toFloat(), within(stDev / 10.0f))
                assertThat(computedStDev)
                        .isCloseTo(stDev.toFloat(), within(stDev * 0.75f))
            }

            scenario("Forge an long array") {
                val size = forger.aTinyInt()
                val data = forger.aLongArray(LongConstraint.NEGATIVE, size)

                assertThat(data)
                        .hasSize(size)
                data.forEach { assertThat(it).isLessThanOrEqualTo(0L) }
            }

            scenario("Forge an long array with min/max") {
                val size = forger.aTinyInt()
                val min = forger.aPositiveLong()
                val max = forger.aLong(min = min + 1)
                val data = forger.aLongArray(min, max, size)

                assertThat(data)
                        .hasSize(size)
                data.forEach { assertThat(it).isGreaterThanOrEqualTo(min).isLessThan(max) }
            }

            scenario("Forge an long array with gaussian distribution") {

                forger.reset(seed)
                println("Seed = $seed")
                val size = 1024 + forger.aTinyInt()
                val mean = forger.aLong(1L, 512L)
                val stDev = forger.aLong(1L, 64L)
                val data = forger.aLongArrayWithDistribution(mean, stDev, size)

                assertThat(data)
                        .hasSize(size)

                var sum = 0f
                var squareSum = 0f
                data.forEach {
                    sum += it
                    squareSum += (it * it)
                }

                val computedMean = sum / size
                val computedStDev = sqrt(abs(squareSum - (size * mean * mean)) / (size - 1.0)).toFloat()

                assertThat(computedMean)
                        .isCloseTo(mean.toFloat(), within(stDev / 10.0f))
                assertThat(computedStDev)
                        .isCloseTo(stDev.toFloat(), within(stDev * 0.75f))
            }

            scenario("Forge a float array") {
                val size = forger.aTinyInt()
                val data = forger.aFloatArray(FloatConstraint.POSITIVE_STRICT, size)

                assertThat(data)
                        .hasSize(size)
                data.forEach { assertThat(it).isGreaterThan(0f) }
            }

            scenario("Forge a float array") {
                val size = forger.aTinyInt()
                val min = forger.aPositiveFloat()
                val max = forger.aFloat(min = min + 1)
                val data = forger.aFloatArray(min, max, size)

                assertThat(data)
                        .hasSize(size)
                data.forEach { assertThat(it).isGreaterThanOrEqualTo(min).isLessThan(max) }
            }

            scenario("Forge a float array with gaussian distribution") {

                forger.reset(seed)
                println("Seed = $seed")
                val size = 1024 + forger.aTinyInt()
                val mean = forger.aFloat(-1000f, 1000f)
                val stDev = forger.aFloat(10f, 500f)
                val data = forger.aFloatArrayWithDistribution(mean, stDev, size)

                assertThat(data)
                        .hasSize(size)

                var sum = 0f
                var squareSum = 0f
                data.forEach {
                    sum += it
                    squareSum += (it * it)
                }

                val computedMean = sum / size
                val computedStDev = sqrt(abs(squareSum - (size * mean * mean)) / (size - 1.0)).toFloat()

                assertThat(computedMean)
                        .isCloseTo(mean, within(stDev / 10.0f))
                assertThat(computedStDev)
                        .isCloseTo(stDev, within(stDev * 0.75f))
            }

            scenario("Forge a double array") {
                val size = forger.aTinyInt()
                val data = forger.aDoubleArray(DoubleConstraint.NEGATIVE_STRICT, size)

                assertThat(data)
                        .hasSize(size)
                data.forEach { assertThat(it).isLessThan(0.0) }
            }

            scenario("Forge a double array") {
                val size = forger.aTinyInt()
                val min = forger.aPositiveDouble()
                val max = forger.aDouble(min = min + 1)
                val data = forger.aDoubleArray(min, max, size)

                assertThat(data)
                        .hasSize(size)
                data.forEach { assertThat(it).isGreaterThanOrEqualTo(min).isLessThan(max) }
            }

            scenario("Forge a double array with gaussian distribution") {

                forger.reset(seed)
                println("Seed = $seed")
                val size = 1024 + forger.aTinyInt()
                val mean = forger.aDouble(-1000.0, 1000.0)
                val stDev = forger.aDouble(10.0, 500.0)
                val data = forger.aDoubleArrayWithDistribution(mean, stDev, size)

                assertThat(data)
                        .hasSize(size)

                var sum = 0.0
                var squareSum = 0.0
                data.forEach {
                    sum += it
                    squareSum += (it * it)
                }

                val computedMean = sum / size
                val computedStDev = sqrt(abs(squareSum - (size * mean * mean)) / (size - 1.0))

                assertThat(computedMean)
                        .isCloseTo(mean, within(stDev / 10.0f))
                assertThat(computedStDev)
                        .isCloseTo(stDev, within(stDev * 0.75f))
            }

            scenario("Forge an char array") {
                val size = forger.aTinyInt()
                val data = forger.aCharArray(CharConstraint.HEXADECIMAL, Case.LOWER, size)

                assertThat(data)
                        .hasSize(size)
                data.forEach { assertThat(Forger.HEXA_LOWER).contains(it) }
            }

            scenario("Forge an char array with min/max") {
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
}