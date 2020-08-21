package fr.xgouchet.elmyr.spek

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeConfigurator
import fr.xgouchet.elmyr.spek.dummy.Bar
import fr.xgouchet.elmyr.spek.dummy.BarFactory
import fr.xgouchet.elmyr.spek.dummy.Foo
import fr.xgouchet.elmyr.spek.dummy.FooFactory
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek

internal class ConfiguredForgeSpek : Spek({

    val forge = spekForge(
        configurator = ConfiguredForgeSpek.Configurator
    )

    group("foo") {

        lateinit var fakeFoo: Foo
        var previousFoo: Foo? = null

        beforeEachTest {
            fakeFoo = forge.getForgery()
        }

        test("A") {
            assertThat(fakeFoo).isNotEqualTo(previousFoo)
            previousFoo = fakeFoo
        }

        test("B") {
            assertThat(fakeFoo).isNotEqualTo(previousFoo)
            previousFoo = fakeFoo
        }

        test("C") {
            assertThat(fakeFoo).isNotEqualTo(previousFoo)
            previousFoo = fakeFoo
        }
    }

    group("Bar") {

        test("E") {
            val bar = forge.getForgery<Bar>()

            assertThat(bar).isNotNull()
        }
    }
}) {

    object Configurator : ForgeConfigurator {
        override fun configure(forge: Forge) {
            forge.addFactory(FooFactory())
            forge.addFactory(BarFactory())
        }
    }
}
