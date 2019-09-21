package fr.xgouchet.elmyr

import fr.xgouchet.elmyr.kotlin.booleanForgery
import fr.xgouchet.elmyr.kotlin.nullableForgery
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe

class ForgeDelegateSpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        beforeEachTest {
            seed = System.nanoTime()
            forge.seed = seed
        }

        context("in a class with properties") {
            val probability = 0.13f
            verifyProbability(
                    expectedProbability = probability.toDouble()
            ) {
                val temp = WithNullable(forge, probability)
                temp.forgedNullableBoolean == null
            }
        }
    }
})

class WithNullable(
    override val forge: Forge,
    probability: Float
) : ForgeryAware {

    internal val forgedNullableBoolean: Boolean? by nullableForgery(booleanForgery(), probability)
}