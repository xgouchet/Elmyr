package fr.xgouchet.elmyr

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import fr.xgouchet.elmyr.fixture.Bar
import fr.xgouchet.elmyr.fixture.Foo
import fr.xgouchet.elmyr.fixture.Food
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ForgeFactorySpek : Spek({

    describe("A forge") {
        val forge = Forge()
        var seed: Long

        beforeEachTest {
            seed = Forge.seed()
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
