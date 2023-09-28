package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeConfigurator
import fr.xgouchet.elmyr.annotation.FloatForgery
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.junit5.fixture.Bar
import fr.xgouchet.elmyr.junit5.fixture.BarFactory
import fr.xgouchet.elmyr.junit5.fixture.Baz
import fr.xgouchet.elmyr.junit5.fixture.Foo
import fr.xgouchet.elmyr.junit5.fixture.FooFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(value = KotlinReproducibilityTest.Configurator::class, seed = KotlinReproducibilityTest.SEED)
internal class KotlinReproducibilityTest {

    @Forgery
    private lateinit var fakeFoo: Foo

    @Forgery
    private lateinit var fakeBar: Bar

    @Forgery
    private lateinit var fakeBaz: Baz

    fun getFoo(): Foo {
        return fakeFoo
    }

    fun getBar(): Bar {
        return fakeBar
    }

    fun getBaz(): Baz {
        return fakeBaz
    }

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
    fun testRun3(@Forgery foo: Foo, @Forgery bar: Bar, @Forgery baz: Baz) {
        assertThat(foo.i).isEqualTo(1959122550)
        assertThat(bar.s).isEqualTo("qrpmpa")
        assertThat(baz.i).isEqualTo(1622758883)
        assertThat(baz.s).isEqualTo("ሑ젇豯锥쏹藾搡\u0A7B蛭孑炷ൺ诘嫶綊뒴꫰癠⻫랕뵆무砑睋鴼䫊᭧萁뉖⧾⭺")
        assertThat(baz.type).isEqualTo(Baz.Type.Y)
    }

    @Test
    fun testRun4(@FloatForgery f: Float, @IntForgery i: Int) {
        assertThat(f).isEqualTo(-3.01852E38f)
        assertThat(i).isEqualTo(1465580921)
    }

    private fun checkSeedNotChanged(forge: Forge) {
        assertThat(forge.seed).isEqualTo(SEED)
    }

    private fun checkForgeryInjected() {
        assertThat(fakeFoo.i).isEqualTo(1955817233)
        assertThat(fakeBar.s).isEqualTo("grquxsqwqccdwk")
        assertThat(fakeBaz.i).isEqualTo(1596512190)
        assertThat(fakeBaz.s).isEqualTo("鞕瑂踜㎅⽓崔ꉨ摣ꯧ踿鼢괒ш駎柨琓懼눫")
        assertThat(fakeBaz.type).isEqualTo(Baz.Type.P)
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
