package fr.xgouchet.elmyr.junit4

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit4.dummy.Bar
import fr.xgouchet.elmyr.junit4.dummy.BarFactory
import fr.xgouchet.elmyr.junit4.dummy.Foo
import fr.xgouchet.elmyr.junit4.dummy.FooFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class KotlinReproducibilityTest {

    @Rule
    @JvmField
    val forge = ForgeRule(SEED)
            .withFactory(FooFactory())
            .withFactory(BarFactory())

    @Forgery
    internal lateinit var fakeFoo: Foo
    @Forgery
    internal lateinit var fakeBar: Bar

    @Before
    fun setUp() {
        checkSeedNotChanged()
        checkForgeryInjected()
    }

    @Test
    fun testRun1() {
    }

    @Test
    fun testRun2() {
    }

    // region Internal

    private fun checkSeedNotChanged() {
        assertThat(forge.seed).isEqualTo(SEED)
    }

    private fun checkForgeryInjected() {
        assertThat(fakeFoo.i).isEqualTo(1570664230)
        assertThat(fakeBar.s).isEqualTo("782ma017p6di4ro6xjj")
    }

    // endregion

    companion object {
        const val SEED = 0x3A563C60126L
    }
}
