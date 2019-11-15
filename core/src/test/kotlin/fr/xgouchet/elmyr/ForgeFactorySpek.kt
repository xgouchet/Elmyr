package fr.xgouchet.elmyr

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import fr.xgouchet.elmyr.dummy.Bar
import fr.xgouchet.elmyr.dummy.Foo
import fr.xgouchet.elmyr.dummy.Food
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class ForgeFactorySpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        beforeEachTest {
            seed = System.nanoTime()
            forge.seed = seed
        }

        context("with a factory") {
            val mockFooFactory: ForgeryFactory<Foo> = mock()
            val mockEggFactory: ForgeryFactory<Food.Egg> = mock()
            val mockBaconFactory: ForgeryFactory<Food.Bacon> = mock()

            forge.addFactory(mockFooFactory)
            forge.addFactory(mockEggFactory)
            forge.addFactory(mockBaconFactory)

            it("forges an instance") {
                val fakeFoo = Foo(forge.anInt())
                whenever(mockFooFactory.getForgery(any())) doReturn fakeFoo

                val foo: Foo = forge.getForgery()

                assertThat(foo).isNotNull().isSameAs(fakeFoo)
            }

            it("fails on unknown classes") {
                try {
                    forge.getForgery<Bar>()
                    throw AssertionError("Should fail here")
                } catch (e: ForgeryException) {
                    // Nothing to do here
                }
            }

            it("falls back on subclasses") {
                val fakeEgg = Food.Egg(forge.aBool())
                val fakeBacon = Food.Bacon(forge.aBool())
                whenever(mockEggFactory.getForgery(any())) doReturn fakeEgg
                whenever(mockBaconFactory.getForgery(any())) doReturn fakeBacon
                val food: Food = forge.getForgery()

                assertThat(food).isIn(fakeEgg, fakeBacon)
            }
        }
    }
})
