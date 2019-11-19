package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeConfigurator
import fr.xgouchet.elmyr.annotation.DoubleForgery
import fr.xgouchet.elmyr.annotation.FloatForgery
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.annotation.LongForgery
import fr.xgouchet.elmyr.junit5.dummy.Foo
import fr.xgouchet.elmyr.junit5.dummy.FooFactory
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(KotlinAnnotationTest.Configurator::class)
open class KotlinAnnotationTest {

    @Forgery
    lateinit var fakeFoo: Foo

    // region Forge

    @Test
    fun testRun1(@Forgery forge: Forge) {
        checkSeedChanged(forge)
        checkForgeryInjectedInField()
    }

    @Test
    fun testRun2(@Forgery forge: Forge) {
        checkSeedChanged(forge)
        checkForgeryInjectedInField()
    }

    @Test
    fun testRun3(forge: Forge) {
        checkSeedChanged(forge)
        checkForgeryInjectedInField()
    }

    // endregion

    // region primitive

    @Test
    fun injectIntWithDefaultRange(@IntForgery i: Int) {
        assertThat(i).isStrictlyBetween(Int.MIN_VALUE, Int.MAX_VALUE)
        checkForgeryInjectedInField()
    }

    @Test
    fun injectIntWithCustomRange(@IntForgery(min = 13, max = 42) i: Int) {
        assertThat(i).isBetween(13, 41)
        checkForgeryInjectedInField()
    }

    @Test
    fun injectIntWithGaussianDistribution(@IntForgery(mean = 42, standardDeviation = 7) i: Int) {
        assertThat(i).isBetween(-58, 142)
        checkForgeryInjectedInField()
    }

    @Test
    fun injectLongWithDefaultRange(@LongForgery l: Long) {
        assertThat(l).isStrictlyBetween(Long.MIN_VALUE, Long.MAX_VALUE)
        checkForgeryInjectedInField()
    }

    @Test
    fun injectLongWithCustomRange(@LongForgery(min = 13L, max = 42L) l: Long) {
        assertThat(l).isBetween(13L, 41L)
        checkForgeryInjectedInField()
    }

    @Test
    fun injectLongWithGaussianDistribution(@LongForgery(mean = 42L, standardDeviation = 7L) l: Long) {
        assertThat(l).isBetween(-58L, 142L)
        checkForgeryInjectedInField()
    }

    @Test
    fun injectFloatWithDefaultRange(@FloatForgery f: Float) {
        assertThat(f).isStrictlyBetween(-Float.MAX_VALUE, Float.MAX_VALUE)
        checkForgeryInjectedInField()
    }

    @Test
    fun injectFloatWithCustomRange(@FloatForgery(min = 13f, max = 42f) f: Float) {
        assertThat(f).isBetween(13f, 42f)
        checkForgeryInjectedInField()
    }

    @Test
    fun injectFloatWithGaussianDistribution(@FloatForgery(mean = 42f, standardDeviation = 7f) f: Float) {
        assertThat(f).isBetween(-58f, 142f)
        checkForgeryInjectedInField()
    }

    @Test
    fun injectDoubleWithDefaultRange(@DoubleForgery d: Double) {
        assertThat(d).isStrictlyBetween(-Double.MAX_VALUE, Double.MAX_VALUE)
        checkForgeryInjectedInField()
    }

    @Test
    fun injectDoubleWithCustomRange(@DoubleForgery(min = 13.0, max = 42.0) d: Double) {
        assertThat(d).isBetween(13.0, 42.0)
        checkForgeryInjectedInField()
    }

    @Test
    fun injectDoubleWithGaussianDistribution(@DoubleForgery(mean = 42.0, standardDeviation = 7.0) d: Double) {
        assertThat(d).isBetween(-58.0, 142.0)
        checkForgeryInjectedInField()
    }

    // endregion

    // region Object from Factory

    @Test
    fun injectForgery(@Forgery foo: Foo) {
        assertThat(foo).isNotNull()
        checkForgeryInjectedInField()
    }

    // endregion

    // region Collections

    @Test
    fun injectCollection(@Forgery list: List<Foo>) {
        assertThat(list).isNotNull().isNotEmpty()

        list.forEach { it: Any ->
            assertThat(it).isInstanceOf(Foo::class.java)
        }
    }

    @Test
    fun injectCollection(@Forgery collection: Collection<Foo>) {
        assertThat(collection).isNotNull().isNotEmpty()

        collection.forEach { it: Any ->
            assertThat(it).isInstanceOf(Foo::class.java)
        }
    }

    @Test
    fun injectList(@Forgery list: List<Foo>) {
        assertThat(list).isNotNull().isNotEmpty()

        list.forEach { it: Any ->
            assertThat(it).isInstanceOf(Foo::class.java)
        }
    }

    @Test
    fun injectSet(@Forgery set: Set<Foo>) {
        assertThat(set).isNotNull().isNotEmpty()

        set.forEach { it: Any ->
            assertThat(it).isInstanceOf(Foo::class.java)
        }
    }

    // endregion

    // region Internal

    private fun checkSeedChanged(forge: Forge) {
        val previousSeed = memoizedSeed
        if (previousSeed != null) {
            Assertions.assertThat(forge.seed).isNotEqualTo(previousSeed)
        }
        memoizedSeed = forge.seed
    }

    private fun checkForgeryInjectedInField() {
        val previousFoo = memoizedFoo
        check(::fakeFoo.isInitialized)
        if (previousFoo != null) {
            Assertions.assertThat(fakeFoo).isNotEqualTo(previousFoo)
        }
        memoizedFoo = fakeFoo
    }

    // endregion

    class Configurator : ForgeConfigurator {
        override fun configure(forge: Forge) {
            forge.addFactory(FooFactory())
        }
    }

    companion object {
        internal var memoizedSeed: Long? = null
        internal var memoizedFoo: Foo? = null
    }
}
