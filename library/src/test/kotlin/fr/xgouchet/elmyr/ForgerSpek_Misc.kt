package fr.xgouchet.elmyr

import org.assertj.core.api.Java6Assertions.assertThat
import org.assertj.core.api.Java6Assertions.within
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.lang.Math.abs
import java.lang.Math.sqrt

/**
 * @author Xavier F. Gouchet
 */
class ForgerSpek_Misc : Spek({

    describe("A forger ") {

        val forger = Forger()
        var seed: Long = 0
        var data: IntArray = IntArray(0)
        val testRepeatCountSmall = 16
        val testRepeatCountHuge = 1024
        val arraySize = 32

        beforeEachTest {
            seed = System.nanoTime()
            forger.reset(seed)
            data = IntArray(arraySize) { forger.anInt(min = it) }
        }

        context("forging nullables ") {

            it("forges nullable value (with 50% probability)") {
                verifyProbability(testRepeatCountHuge,
                        0.5,
                        {
                            val str = forger.aString()
                            val res = forger.aNullableFrom(str)
                            res == null
                        })

            }

            it("forges nullable value with probability") {

                val probability = forger.aFloat(0f, 1f)
                verifyProbability(testRepeatCountHuge,
                        probability.toDouble(),
                        {
                            val str = forger.aString()
                            val res = forger.aNullableFrom(str, probability)
                            res == null
                        })
            }
        }


    }
})
