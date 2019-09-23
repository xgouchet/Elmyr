package fr.xgouchet.elmyr

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class ForgeBooleanSpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        beforeEachTest {
            seed = System.nanoTime()
            forge.seed = seed
        }

        context("forging booleans ") {

            it("forges random booleans (with 50% probability)") {
                verifyProbability(Forge.HALF_PROBABILITY) { forge.aBool() }
            }

            it("forges random boolean with probability") {
                val probability = 0.13f // TODO random
                verifyProbability(probability) { forge.aBool(probability) }
            }
        }
    }
})
