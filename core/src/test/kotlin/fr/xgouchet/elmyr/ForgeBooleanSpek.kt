package fr.xgouchet.elmyr

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ForgeBooleanSpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        beforeEachTest {
            seed = Forge.seed()
            forge.seed = seed
        }

        context("forging booleans ") {

            it("forges random booleans (with 50% probability)") {
                verifyProbability(Forge.HALF_PROBABILITY) { forge.aBool() }
            }

            it("forges random boolean with probability") {
                val probability = forge.aFloat(0f, 1f)
                verifyProbability(probability) { forge.aBool(probability) }
            }
        }
    }
})
