package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.dummy.Foo
import fr.xgouchet.elmyr.junit5.dummy.FooFactory
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class KotlinAnnotationTest {

    @Forgery
    lateinit var fakeFoo: Foo

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
    fun injectForgery(@Forgery foo: Foo) {
        assertThat(foo).isNotNull()
        checkForgeryInjectedInField()
    }

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

    companion object {
        @RegisterExtension
        @JvmField
        val FORGE = ForgeExtension()
                .withFactory(FooFactory())

        internal var memoizedSeed: Long? = null
        internal var memoizedFoo: Foo? = null
    }
}