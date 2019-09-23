package fr.xgouchet.elmyr

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import fr.xgouchet.elmyr.kotlin.booleanForgery
import fr.xgouchet.elmyr.kotlin.factoryForgery
import fr.xgouchet.elmyr.kotlin.nullableForgery
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class ForgeDelegateSpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        beforeEachTest {
            seed = System.nanoTime()
            forge.seed = seed
        }

        // region common delegates rules

        // TODO test value same until seed is changed

        // endregion

        // region specific delegates

        context("in a class with nullable properties") {

            it("forges a nullable instance") {
                val probability = 0.13f // TODO random
                verifyProbability(expectedProbability = probability) {
                    val temp = WithNullable(forge, probability)
                    temp.forgedNullableBoolean == null
                }
            }

            it("forges a nullable instance with default probability") {
                val probability = 0.13f // TODO random
                verifyProbability(expectedProbability = Forge.HALF_PROBABILITY) {
                    val temp = WithNullable(forge, probability)
                    temp.forgedNullableBooleanHalf == null
                }
            }

            it("forges a nullable instance from Lambda") {
                val probability = 0.13f // TODO random
                verifyProbability(expectedProbability = probability) {
                    val temp = WithNullable(forge, probability)
                    temp.forgedNullableInt == null
                }
            }

            it("forges a nullable instance from Lambda with default probability") {
                val probability = 0.13f // TODO random
                verifyProbability(expectedProbability = Forge.HALF_PROBABILITY) {
                    val temp = WithNullable(forge, probability)
                    temp.forgedNullableIntHalf == null
                }
            }
        }

        context("in a class with boolean properties") {

            it("forges a boolean instance") {
                val probability = 0.13f // TODO random
                verifyProbability(
                        expectedProbability = probability
                ) {
                    val temp = WithBoolean(forge, probability)
                    temp.forgedBoolean
                }
            }
        }

        context("in a class with factory properties") {
            val mockFooFactory: ForgeryFactory<Foo> = mock()
            forge.addFactory(mockFooFactory)

            it("forges an instance") {
                val fakeFoo = Foo(forge.aBool())
                whenever(mockFooFactory.getForgery(any())) doReturn fakeFoo

                val temp = WithFactory(forge)
                assertThat(temp.forgedFoo).isSameAs(fakeFoo)
            }

            it("fails when factory not available") {
                val temp = WithFactory(forge)
                throws<IllegalArgumentException> {
                    @Suppress("UNUSED_VARIABLE")
                    val b = temp.forgedBar
                }
            }
        }

        // endregion
    }
})

class WithNullable(
    override val forge: Forge,
    probability: Float
) : ForgeryAware {

    internal val forgedNullableBooleanHalf: Boolean? by nullableForgery(booleanForgery())

    internal val forgedNullableIntHalf: Int? by nullableForgery() { aTinyInt() }

    internal val forgedNullableBoolean: Boolean? by nullableForgery(booleanForgery(), probability)

    internal val forgedNullableInt: Int? by nullableForgery(probability) { aTinyInt() }
}

class WithBoolean(
    override val forge: Forge,
    probability: Float
) : ForgeryAware {

    internal val forgedBoolean: Boolean by booleanForgery(probability)
}

class WithFactory(
    override val forge: Forge
) : ForgeryAware {

    internal val forgedFoo: Foo by factoryForgery()

    internal val forgedBar: Bar by factoryForgery()
}