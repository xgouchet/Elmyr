package fr.xgouchet.elmyr

import fr.xgouchet.elmyr.dummy.Foo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ForgeNullableSpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        beforeEachTest {
            seed = Forge.seed()
            forge.seed = seed
        }

        context("forging nullable values ") {
            val fooFactory = object : ForgeryFactory<Foo> {
                override fun getForgery(forge: Forge): Foo {
                    return Foo(forge.anInt())
                }
            }
            forge.addFactory(fooFactory)

            it("forges random nullable (with 50% probability)") {
                verifyProbability(Forge.HALF_PROBABILITY) {
                    forge.aNullable { anAlphabeticalString() } == null
                }
            }

            it("forges random nullable with probability") {
                val probability = forge.aFloat(0f, 1f)
                verifyProbability(probability) {
                    forge.aNullable(probability) { anAlphabeticalString() } == null
                }
            }

            it("forges random nullable from factory not inlined (with 50% probability)") {
                verifyProbability(Forge.HALF_PROBABILITY) {
                    forge.aNullable(Foo::class.java) == null
                }
            }

            it("forges random nullable from factory not inlined with probability") {
                val probability = forge.aFloat(0f, 1f)
                verifyProbability(probability) {
                    forge.aNullable(Foo::class.java, probability) == null
                }
            }

            it("forges random nullable from factory (with 50% probability)") {
                verifyProbability(Forge.HALF_PROBABILITY) {
                    forge.aNullable<Foo>() == null
                }
            }

            it("forges random nullable from factory with probability") {
                val probability = forge.aFloat(0f, 1f)
                verifyProbability(probability) {
                    forge.aNullable<Foo>(probability) == null
                }
            }
        }
    }
})
