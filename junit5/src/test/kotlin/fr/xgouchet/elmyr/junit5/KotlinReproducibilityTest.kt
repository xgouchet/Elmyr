package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeConfigurator
import fr.xgouchet.elmyr.annotation.FloatForgery
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.junit5.dummy.Bar
import fr.xgouchet.elmyr.junit5.dummy.BarFactory
import fr.xgouchet.elmyr.junit5.dummy.Foo
import fr.xgouchet.elmyr.junit5.dummy.FooFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(value = KotlinReproducibilityTest.Configurator::class, seed = KotlinReproducibilityTest.SEED)
class KotlinReproducibilityTest {

    @Forgery
    private lateinit var fakeFoo: Foo
    @Forgery
    private lateinit var fakeBar: Bar

    @BeforeEach
    fun setUp(forge: Forge) {
        checkSeedNotChanged(forge)
        checkForgeryInjected()
    }

    @Test
    fun testRun1() {
    }

    @Test
    fun testRun2() {
    }

    @Test
    fun testRun3(@Forgery foo: Foo, @Forgery bar: Bar) {
        assertThat(foo.i).isEqualTo(1834174735)
        assertThat(bar.s).isEqualTo("wfpwhlvm")
    }

    @Test
    fun testRun4(@FloatForgery f: Float, @IntForgery i: Int) {
        assertThat(f).isEqualTo(2.5332062E38f)
        assertThat(i).isEqualTo(-1217237951)
    }

    private fun checkSeedNotChanged(forge: Forge) {
        assertThat(forge.seed).isEqualTo(SEED)
    }

    private fun checkForgeryInjected() {
        assertThat(fakeFoo.i).isEqualTo(1596512190)
        assertThat(fakeBar.s).isEqualTo("grquxsqwqccdwk")
    }

    class Configurator : ForgeConfigurator {
        override fun configure(forge: Forge) {
            forge.addFactory(Foo::class.java, FooFactory())
            forge.addFactory(Bar::class.java, BarFactory())
        }
    }
    companion object {
        const val SEED = 0xD774670189EL
    }
}