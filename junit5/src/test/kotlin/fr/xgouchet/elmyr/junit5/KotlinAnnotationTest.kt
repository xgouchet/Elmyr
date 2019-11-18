package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeConfigurator
import fr.xgouchet.elmyr.annotation.Forgery
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
