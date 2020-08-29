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
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedInvalidPrimitives
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedMissingFactory
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedPrimitives
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedRegex
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedStrings
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedUnknownAnnotation
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedUnknownGeneric
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
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
    fun failsWhenInvalidPrimitiveParameters() {
        val injected = KotlinInjectedInvalidPrimitives()

        assertThrows<ForgeryException> {
            injector.inject(forge, injected)
        }
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

    @Test
    fun ignoresWhenUnknownAnnotation() {
        val injected = KotlinInjectedUnknownAnnotation()

        injector.inject(forge, injected)

        assertThat(injected.retrieveFooOrNull()).isNull()
    }

    @Test
    fun injectPrimitiveBoolean() {
        val injected = KotlinInjectedPrimitives()

        injector.inject(forge, injected)

        assertThat(injected.publicBoolList).isNotEmpty().hasDistinctValues()
        assertThat(injected.publicBoolSet).isNotEmpty().hasDistinctValues()
        assertThat(injected.publicBoolCollection).isNotEmpty().hasDistinctValues()
    }

    @Test
    fun injectPrimitiveInt() {
        val injected = KotlinInjectedPrimitives()

        injector.inject(forge, injected)

        assertThat(injected.publicInt).isNotEqualTo(0)
        assertThat(injected.publicRangeInt).isBetween(3, 42)
        assertThat(injected.publicGaussianInt).isCloseTo(1337, Offset.offset(20))
        assertThat(injected.internalInt).isNotEqualTo(0)
        assertThat(injected.retrieveProtectedInt()).isNotEqualTo(0)
        assertThat(injected.retrievePrivateInt()).isNotEqualTo(0)
        assertThat(injected.publicIntList).isNotEmpty().hasDistinctValues()
        assertThat(injected.publicIntSet).isNotEmpty().hasDistinctValues()
        assertThat(injected.publicIntCollection).isNotEmpty().hasDistinctValues()
    }

    @Test
    fun injectPrimitiveLong() {
        val injected = KotlinInjectedPrimitives()

        injector.inject(forge, injected)

        assertThat(injected.publicLong).isNotEqualTo(0)
        assertThat(injected.publicRangeLong).isBetween(3, 42)
        assertThat(injected.publicGaussianLong).isCloseTo(1337L, Offset.offset(20L))
        assertThat(injected.internalLong).isNotEqualTo(0)
        assertThat(injected.retrieveProtectedLong()).isNotEqualTo(0)
        assertThat(injected.retrievePrivateLong()).isNotEqualTo(0)
        assertThat(injected.publicLongList).isNotEmpty().hasDistinctValues()
        assertThat(injected.publicLongSet).isNotEmpty().hasDistinctValues()
        assertThat(injected.publicLongCollection).isNotEmpty().hasDistinctValues()
    }

    @Test
    fun injectPrimitiveFloat() {
        val injected = KotlinInjectedPrimitives()

        injector.inject(forge, injected)

        assertThat(injected.publicFloat).isNotEqualTo(0)
        assertThat(injected.publicRangeFloat).isBetween(3f, 42f)
        assertThat(injected.publicGaussianFloat).isCloseTo(1337f, Offset.offset(20f))
        assertThat(injected.internalFloat).isNotEqualTo(0)
        assertThat(injected.retrieveProtectedFloat()).isNotEqualTo(0)
        assertThat(injected.retrievePrivateFloat()).isNotEqualTo(0)
        assertThat(injected.publicFloatList).isNotEmpty().hasDistinctValues()
        assertThat(injected.publicFloatSet).isNotEmpty().hasDistinctValues()
        assertThat(injected.publicFloatCollection).isNotEmpty().hasDistinctValues()
    }

    @Test
    fun injectPrimitiveDouble() {
        val injected = KotlinInjectedPrimitives()

        injector.inject(forge, injected)

        assertThat(injected.publicDouble).isNotEqualTo(0)
        assertThat(injected.publicRangeDouble).isBetween(3.0, 42.0)
        assertThat(injected.publicGaussianDouble).isCloseTo(1337.0, Offset.offset(20.0))
        assertThat(injected.internalDouble).isNotEqualTo(0)
        assertThat(injected.retrieveProtectedDouble()).isNotEqualTo(0)
        assertThat(injected.retrievePrivateDouble()).isNotEqualTo(0)
        assertThat(injected.publicDoubleList).isNotEmpty().hasDistinctValues()
        assertThat(injected.publicDoubleSet).isNotEmpty().hasDistinctValues()
        assertThat(injected.publicDoubleCollection).isNotEmpty().hasDistinctValues()
    }

    @Test
    fun injectStrings() {
        val injected = KotlinInjectedStrings()

        injector.inject(forge, injected)

        assertThat(injected.publicAplhaString).matches("[a-zA-Z]+")
        assertThat(injected.publicAplhaLowerString).matches("[a-z]+")
        assertThat(injected.publicAplhaUpperString).matches("[A-Z]+")
        assertThat(injected.publicAplhaNumString).matches("[a-zA-Z0-9]+")
        assertThat(injected.publicDigitsString).matches("[0-9]+")
        assertThat(injected.retrieveProtectedHexString()).matches("[a-fA-F0-9]+")
        assertThat(injected.retrievePrivateWhitespaceString()).matches("\\s+")

        assertThat(injected.publicAlphaStringList).isNotEmpty()
            .hasDistinctValues()
            .allMatch { it.matches(Regex("[a-zA-Z]+")) }
        assertThat(injected.publicHexaStringSet.toList()).isNotEmpty()
            .hasDistinctValues()
            .allMatch { it.matches(Regex("[a-fA-F0-9]+")) }
        assertThat(injected.publicNumStringCollection.toList()).isNotEmpty()
            .hasDistinctValues()
            .allMatch { it.matches(Regex("[0-9]+")) }
    }

    @Test
    fun injectRegex() {
        val injected = KotlinInjectedRegex()

        injector.inject(forge, injected)

        assertThat(injected.publicAlphaString).matches("[a-z]+")
        assertThat(injected.internalDigitsString).matches("[0-9]+")
        assertThat(injected.retrieveProtectedBase64String()).matches("([a-zA-z0-9]{4})*[a-zA-z0-9]{2}==")
        assertThat(injected.retrievePrivatePhoneNumber()).matches("0\\d(-\\d\\d){4}")
    }
}

private fun <
    SELF : AbstractAssert<SELF, ACTUAL>,
    ACTUAL : Collection<T>,
    T : Comparable<T>
    > AbstractAssert<SELF, ACTUAL>.hasDistinctValues(minLength: Int = 3): SELF {
    return matches {
        if (it.size > minLength) {
            it.sorted().distinct().size > 1
        } else {
            true
        }
    }
}
