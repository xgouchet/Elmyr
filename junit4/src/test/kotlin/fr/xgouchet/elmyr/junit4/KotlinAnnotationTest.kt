package fr.xgouchet.elmyr.junit4

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit4.dummy.Foo
import fr.xgouchet.elmyr.junit4.dummy.FooFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class KotlinAnnotationTest {

    @Rule
    @JvmField
    val forge = ForgeRule().withFactory(FooFactory())

    @Forgery
    internal lateinit var fakeFoo: Foo

    @Before
    fun setUp() {
        checkSeedChanged()
        checkForgeryInjected()
    }

    @Test
    fun testRun1() {
    }

    @Test
    fun testRun2() {
    }

    // region Internal

    private fun checkSeedChanged() {
        val previousSeed = memoizedSeed
        if (previousSeed != null) {
            assertThat(forge.seed).isNotEqualTo(previousSeed)
        }
        memoizedSeed = forge.seed
    }

    private fun checkForgeryInjected() {
        val previousFoo = memoizedFoo
        if (previousFoo != null) {
            assertThat(fakeFoo).isNotEqualTo(previousFoo)
        }
        memoizedFoo = fakeFoo
    }

    // endregion

    companion object {
        internal var memoizedSeed: Long? = null
        internal var memoizedFoo: Foo? = null
    }
}
