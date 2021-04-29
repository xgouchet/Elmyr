package fr.xgouchet.elmyr

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import fr.xgouchet.elmyr.fixture.Bar
import fr.xgouchet.elmyr.fixture.Foo
import fr.xgouchet.elmyr.kotlin.BooleanProperty.Companion.booleanForgery
import fr.xgouchet.elmyr.kotlin.DoubleProperty.Companion.doubleForgery
import fr.xgouchet.elmyr.kotlin.FactoryListProperty.Companion.factoryListForgery
import fr.xgouchet.elmyr.kotlin.FactoryProperty.Companion.factoryForgery
import fr.xgouchet.elmyr.kotlin.FactorySetProperty.Companion.factorySetForgery
import fr.xgouchet.elmyr.kotlin.FloatProperty.Companion.floatForgery
import fr.xgouchet.elmyr.kotlin.IntProperty.Companion.intForgery
import fr.xgouchet.elmyr.kotlin.LongProperty.Companion.longForgery
import fr.xgouchet.elmyr.kotlin.NullableProperty.Companion.nullableForgery
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ForgeDelegateSpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        val testRepeatCountSmall = 16

        beforeEachTest {
            seed = Forge.seed()
            forge.seed = seed
        }

        // region common delegates rules

        context("in a class with delegate properties") {

            it("forges a new value when the forge seed changes") {

                var same = 0
                val count = 32
                val temp = WithPrimitives(forge)
                var previous = temp.forgedInt

                repeat(count) {
                    forge.seed = Forge.seed()
                    val next = temp.forgedInt
                    if (next == previous) same ++
                    previous = next
                }

                assertThat(same).isLessThan(count / 4)
            }

            it("reuses the same value when the same forge seed is used") {

                val temp = WithPrimitives(forge)
                val first = temp.forgedInt
                val second = temp.forgedInt

                assertThat(second).isEqualTo(first)
            }
        }

        // endregion

        // region specific delegates

        context("in a class with nullable properties") {

            it("forges a nullable instance") {
                val probability = forge.aFloat(0f, 1f)
                verifyProbability(expectedProbability = probability) {
                    val temp = WithNullable(forge, probability)
                    temp.forgedNullableBoolean == null
                }
            }

            it("forges a nullable instance with default probability") {
                val probability = forge.aFloat(0f, 1f)
                verifyProbability(expectedProbability = Forge.HALF_PROBABILITY) {
                    val temp = WithNullable(forge, probability)
                    temp.forgedNullableBooleanHalf == null
                }
            }

            it("forges a nullable instance from Lambda") {
                val probability = forge.aFloat(0f, 1f)
                verifyProbability(expectedProbability = probability) {
                    val temp = WithNullable(forge, probability)
                    temp.forgedNullableInt == null
                }
            }

            it("forges a nullable instance from Lambda with default probability") {
                val probability = forge.aFloat(0f, 1f)
                verifyProbability(expectedProbability = Forge.HALF_PROBABILITY) {
                    val temp = WithNullable(forge, probability)
                    temp.forgedNullableIntHalf == null
                }
            }
        }

        context("in a class with primitive properties") {

            it("forges a boolean instance") {
                val probability = forge.aFloat(0f, 1f)
                verifyProbability(
                    expectedProbability = probability
                ) {
                    val temp = WithPrimitives(forge, probability)
                    temp.forgedBoolean
                }
            }

            it("forges an int instance") {
                repeat(testRepeatCountSmall) {
                    val temp = WithPrimitives(forge)
                    assertThat(temp.forgedInt)
                        .isBetween(13, 42)
                }
            }

            it("forges a long instance") {
                repeat(testRepeatCountSmall) {
                    val temp = WithPrimitives(forge)
                    assertThat(temp.forgedLong)
                        .isBetween(1337L, 4815162342L)
                }
            }

            it("forges a float instance") {
                repeat(testRepeatCountSmall) {
                    val temp = WithPrimitives(forge)
                    assertThat(temp.forgedFloat)
                        .isBetween(2.7f, 3.142f)
                }
            }

            it("forges a double instance") {
                repeat(testRepeatCountSmall) {
                    val temp = WithPrimitives(forge)
                    assertThat(temp.forgedDouble)
                        .isBetween(2.7, 3.142)
                }
            }
        }

        context("in a class with factory properties") {
            val mockFooFactory: ForgeryFactory<Foo> = mock()
            forge.addFactory(mockFooFactory)

            it("forges an instance") {
                val fakeFoo = Foo(forge.anInt())
                whenever(mockFooFactory.getForgery(any())) doReturn fakeFoo

                val temp = WithFactory(forge)
                assertThat(temp.forgedFoo).isSameAs(fakeFoo)
            }

            it("forges a list") {
                val fakeFoo = Foo(forge.anInt())
                whenever(mockFooFactory.getForgery(any())) doReturn fakeFoo

                val temp = WithFactory(forge)
                assertThat(temp.forgedFooList)
                    .containsOnly(fakeFoo)
            }

            it("forges a set") {
                val fakeFoo = Foo(forge.anInt())
                whenever(mockFooFactory.getForgery(any())) doReturn fakeFoo

                val temp = WithFactory(forge)
                assertThat(temp.forgedFooSet)
                    .containsOnly(fakeFoo)
            }

            it("fails when factory not available") {
                val temp = WithFactory(forge)
                throws<ForgeryException> {
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

class WithPrimitives(
    override val forge: Forge,
    probability: Float = 0.5f
) : ForgeryAware {

    internal val forgedBoolean: Boolean by booleanForgery(probability)

    internal val forgedInt: Int by intForgery(13, 42)

    internal val forgedLong: Long by longForgery(1337L, 4815162342L)

    internal val forgedFloat: Float by floatForgery(2.718281f, 3.141592f)

    internal val forgedDouble: Double by doubleForgery(2.718281, 3.141592)
}

class WithFactory(
    override val forge: Forge
) : ForgeryAware {

    internal val forgedFoo: Foo by factoryForgery()

    internal val forgedBar: Bar by factoryForgery()

    internal val forgedFooList: List<Foo> by factoryListForgery()

    internal val forgedFooSet: Set<Foo> by factorySetForgery()
}
