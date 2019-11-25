package fr.xgouchet.elmyr.inject

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryException
import fr.xgouchet.elmyr.inject.dummy.Bar
import fr.xgouchet.elmyr.inject.dummy.BarFactory
import fr.xgouchet.elmyr.inject.dummy.Foo
import fr.xgouchet.elmyr.inject.dummy.FooFactory
import fr.xgouchet.elmyr.inject.dummy.KotlinInjected
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedChild
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedGenerics
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedImmutableVal
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedMissingFactory
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedUnknownGeneric
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class KotlinForgeryInjectorTest {

    private lateinit var injector: ForgeryInjector
    private lateinit var forge: Forge

    @BeforeEach
    fun setUp() {
        injector = DefaultForgeryInjector()
        forge = Forge().apply {
            addFactory(Foo::class.java, FooFactory())
            addFactory(Bar::class.java, BarFactory())
        }
    }

    @Test
    fun injectsPublicProperty() {
        val injected = KotlinInjected()

        injector.inject(forge, injected)

        assertThat(injected.publicFoo).isNotNull()
    }

    @Test
    fun injectsPackageProperty() {
        val injected = KotlinInjected()

        injector.inject(forge, injected)

        assertThat(injected.internalFoo).isNotNull()
    }

    @Test
    fun injectsProtectedProperty() {
        val injected = KotlinInjected()

        injector.inject(forge, injected)

        assertThat(injected.retrieveProtectedFoo()).isNotNull()
    }

    @Test
    fun injectsPrivateProperty() {
        val injected = KotlinInjected()

        injector.inject(forge, injected)

        assertThat(injected.retrievePrivateFoo()).isNotNull()
    }

    @Test
    fun injectsDifferentInstances() {
        val injected = KotlinInjected()

        injector.inject(forge, injected)

        assertThat(injected.publicFoo)
                .isNotSameAs(injected.internalFoo)
                .isNotSameAs(injected.retrieveProtectedFoo())
                .isNotSameAs(injected.retrievePrivateFoo())

        assertThat(injected.internalFoo)
                .isNotSameAs(injected.retrieveProtectedFoo())
                .isNotSameAs(injected.retrievePrivateFoo())

        assertThat(injected.retrieveProtectedFoo())
                .isNotSameAs(injected.retrievePrivateFoo())
    }

    @Test
    fun injectsPropertiesFromParentClass() {
        val injected = KotlinInjectedChild()

        injector.inject(forge, injected)

        assertThat(injected.publicFoo).isNotNull()
        assertThat(injected.internalFoo).isNotNull()
        assertThat(injected.retrieveProtectedFoo()).isNotNull()
        assertThat(injected.retrievePrivateFoo()).isNotNull()
        assertThat(injected.childFoo).isNotNull()
    }

    @Test
    fun injectsGenerics() {
        val injected = KotlinInjectedGenerics()

        injector.inject(forge, injected)

        assertThat(injected.publicFooList).isNotNull.isNotEmpty
        assertThat(injected.publicFooSet).isNotNull.isNotEmpty
        assertThat(injected.publicFooMap).isNotNull.isNotEmpty
        assertThat(injected.publicFooColletion).isNotNull.isNotEmpty
    }

    @Test
    fun failsWhenMissingFactory() {
        val injected = KotlinInjectedMissingFactory()

        assertThrows<ForgeryException> {
            injector.inject(forge, injected)
        }
    }

    @Test
    fun failsWhenImmutableProperty() {
        val injected = KotlinInjectedImmutableVal()

        assertThrows<ForgeryInjectorException> {
            injector.inject(forge, injected)
        }
    }

    @Test
    fun failsWhenUnknownGeneric() {
        val injected = KotlinInjectedUnknownGeneric()

        assertThrows<ForgeryInjectorException> {
            injector.inject(forge, injected)
        }
    }
}
