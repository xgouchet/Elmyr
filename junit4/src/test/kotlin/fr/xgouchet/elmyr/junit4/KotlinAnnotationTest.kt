package fr.xgouchet.elmyr.junit4

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit4.fixture.Bar
import fr.xgouchet.elmyr.junit4.fixture.BarFactory
import fr.xgouchet.elmyr.junit4.fixture.Baz
import fr.xgouchet.elmyr.junit4.fixture.Foo
import fr.xgouchet.elmyr.junit4.fixture.FooFactory
import java.time.Month
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class KotlinAnnotationTest {

    @Rule
    @JvmField
    val forge = ForgeRule()
        .withFactory(FooFactory())
        .withFactory(BarFactory())

    @Forgery
    internal lateinit var fakeFoo: Foo

    @Forgery
    lateinit var fakeFooList: List<Foo>

    @Forgery
    lateinit var fakeFooSet: Set<Foo>

    @Forgery
    lateinit var fakeFooMap: Map<Foo, Bar>

    @Forgery
    lateinit var fakeMonth: Month

    @Forgery
    lateinit var fakeBaz: Baz

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
        assertThat(fakeFoo).isNotNull()

        assertThat(fakeFooList).isNotNull.isNotEmpty
        assertThat(fakeFooSet).isNotNull.isNotEmpty
        assertThat(fakeFooMap).isNotNull.isNotEmpty

        assertThat(fakeMonth).isNotNull()

        assertThat(fakeBaz).isNotNull()
        val previousBaz = memoizedBaz
        if (previousBaz != null) {
            assertThat(fakeBaz).isNotEqualTo(previousBaz)
        }
    }

    // endregion

    companion object {
        internal var memoizedSeed: Long? = null
        internal var memoizedFoo: Foo? = null
        internal var memoizedBaz: Baz? = null
    }
}
