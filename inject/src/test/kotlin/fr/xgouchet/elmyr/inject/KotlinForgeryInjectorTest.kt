package fr.xgouchet.elmyr.inject

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryException
import fr.xgouchet.elmyr.inject.dummy.Bar
import fr.xgouchet.elmyr.inject.dummy.BarFactory
import fr.xgouchet.elmyr.inject.dummy.Foo
import fr.xgouchet.elmyr.inject.dummy.FooFactory
import fr.xgouchet.elmyr.inject.dummy.KotlinInjected
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedAdvanced
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedChild
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedGenerics
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedImmutableVal
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedInvalidPrimitives
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedMap
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedMissingFactory
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedPrimitives
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedRegex
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedStrings
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedUnknownAnnotation
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedUnknownGeneric
import fr.xgouchet.elmyr.inject.dummy.KotlinInjectedUnknownPrimitiveGeneric
import kotlin.math.abs
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class KotlinForgeryInjectorTest {

    private lateinit var injector: ForgeryInjector
    private lateinit var forge: Forge

    @Mock
    lateinit var mockListener: ForgeryInjector.Listener

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

        injector.inject(forge, injected, null)

        assertThat(injected.publicFoo).isNotNull()
    }

    @Test
    fun injectsPackageProperty() {
        val injected = KotlinInjected()

        injector.inject(forge, injected, null)

        assertThat(injected.internalFoo).isNotNull()
    }

    @Test
    fun injectsProtectedProperty() {
        val injected = KotlinInjected()

        injector.inject(forge, injected, null)

        assertThat(injected.retrieveProtectedFoo()).isNotNull()
    }

    @Test
    fun injectsPrivateProperty() {
        val injected = KotlinInjected()

        injector.inject(forge, injected, null)

        assertThat(injected.retrievePrivateFoo()).isNotNull()
    }

    @Test
    fun callsListener() {
        val injected = KotlinInjected()

        injector.inject(forge, injected, mockListener)

        verify(mockListener).onFieldInjected(
            KotlinInjected::class.java,
            Foo::class.java,
            "publicFoo",
            injected.publicFoo
        )
        verify(mockListener).onFieldInjected(
            KotlinInjected::class.java,
            Foo::class.java,
            "internalFoo",
            injected.internalFoo
        )
        verify(mockListener).onFieldInjected(
            KotlinInjected::class.java,
            Foo::class.java,
            "protectedFoo",
            injected.retrieveProtectedFoo()
        )
        verify(mockListener).onFieldInjected(
            KotlinInjected::class.java,
            Foo::class.java,
            "privateFoo",
            injected.retrievePrivateFoo()
        )
        verifyNoMoreInteractions(mockListener)
    }

    @Test
    fun injectsDifferentInstances() {
        val injected = KotlinInjected()

        injector.inject(forge, injected, null)

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

        injector.inject(forge, injected, null)

        assertThat(injected.publicFoo).isNotNull()
        assertThat(injected.internalFoo).isNotNull()
        assertThat(injected.retrieveProtectedFoo()).isNotNull()
        assertThat(injected.retrievePrivateFoo()).isNotNull()
        assertThat(injected.childFoo).isNotNull()
    }

    @Test
    fun injectsGenerics() {
        val injected = KotlinInjectedGenerics()

        injector.inject(forge, injected, null)

        assertThat(injected.publicFooList).isNotNull.isNotEmpty
        assertThat(injected.publicFooSet).isNotNull.isNotEmpty
        assertThat(injected.publicFooMap).isNotNull.isNotEmpty
        assertThat(injected.publicFooColletion).isNotNull.isNotEmpty
    }

    @Test
    fun failsWhenInvalidPrimitiveParameters() {
        val injected = KotlinInjectedInvalidPrimitives()

        assertThrows<ForgeryException> {
            injector.inject(forge, injected, null)
        }
    }

    @Test
    fun failsWhenMissingFactory() {
        val injected = KotlinInjectedMissingFactory()

        assertThrows<ForgeryException> {
            injector.inject(forge, injected, null)
        }
    }

    @Test
    fun failsWhenImmutableProperty() {
        val injected = KotlinInjectedImmutableVal()

        assertThrows<ForgeryInjectorException> {
            injector.inject(forge, injected, null)
        }
    }

    @Test
    fun failsWhenUnknownGeneric() {
        val injected = KotlinInjectedUnknownGeneric()

        assertThrows<ForgeryInjectorException> {
            injector.inject(forge, injected, null)
        }
    }

    @Test
    fun failsWhenUnknownPrimitiveGeneric() {
        val injected = KotlinInjectedUnknownPrimitiveGeneric()

        assertThrows<ForgeryInjectorException> {
            injector.inject(forge, injected, null)
        }
    }

    @Test
    fun ignoresWhenUnknownAnnotation() {
        val injected = KotlinInjectedUnknownAnnotation()

        injector.inject(forge, injected, null)

        assertThat(injected.retrieveFooOrNull()).isNull()
    }

    @Test
    fun injectPrimitiveBoolean() {
        val injected = KotlinInjectedPrimitives()

        injector.inject(forge, injected, null)

        assertThat(injected.publicBoolList).isNotEmpty().hasDistinctValues(16)
        assertThat(injected.publicBoolSet).isNotEmpty().hasDistinctValues(16)
        assertThat(injected.publicBoolCollection).isNotEmpty().hasDistinctValues(16)
    }

    @Test
    fun injectPrimitiveInt() {
        val injected = KotlinInjectedPrimitives()

        injector.inject(forge, injected, null)

        assertThat(injected.publicInt).isNotEqualTo(0)
        assertThat(injected.publicRangeInt).isBetween(3, 42)
        assertThat(injected.publicGaussianInt).isCloseTo(1337, Offset.offset(30))
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

        injector.inject(forge, injected, null)

        assertThat(injected.publicLong).isNotEqualTo(0)
        assertThat(injected.publicRangeLong).isBetween(3, 42)
        assertThat(injected.publicGaussianLong).isCloseTo(1337L, Offset.offset(30L))
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

        injector.inject(forge, injected, null)

        assertThat(injected.publicFloat).isNotEqualTo(0)
        assertThat(injected.publicRangeFloat).isBetween(3f, 43f)
        assertThat(injected.publicGaussianFloat).isCloseTo(1337f, Offset.offset(30f))
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

        injector.inject(forge, injected, null)

        assertThat(injected.publicDouble).isNotEqualTo(0)
        assertThat(injected.publicRangeDouble).isBetween(3.0, 43.0)
        assertThat(injected.publicGaussianDouble).isCloseTo(1337.0, Offset.offset(30.0))
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

        injector.inject(forge, injected, null)

        assertThat(injected.publicAplhaString).matches("[a-zA-Z]+")
        assertThat(injected.publicAplhaLowerString).matches("[a-z]+")
        assertThat(injected.publicAplhaUpperString).matches("[A-Z]+")
        assertThat(injected.publicAplhaNumString).matches("[a-zA-Z0-9]{42}")
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

        injector.inject(forge, injected, null)

        assertThat(injected.publicAlphaString).matches("[a-z]+")
        assertThat(injected.internalDigitsString).matches("[0-9]+")
        assertThat(injected.retrieveProtectedBase64String()).matches("([a-zA-z0-9]{4})*[a-zA-z0-9]{2}==")
        assertThat(injected.retrievePrivatePhoneNumber()).matches("0\\d(-\\d\\d){4}")
        assertThat(injected.publicEmail).matches("[a-z]+@[a-z]+\\.[a-z]{3}")
    }

    @Test
    fun injectAdvanced() {
        val injected = KotlinInjectedAdvanced()

        injector.inject(forge, injected, null)

        assertThat(injected.publicAlphaOrNumString).matches("([a-zA-Z]+)|([0-9]+)")
        assertThat(injected.publicAlphaOrNumStringList).allMatch {
            it is String && it.matches(Regex("([a-zA-Z]+)|([0-9]+)"))
        }
        assertThat(injected.publicMultipleRangesInt).matches { it in 20..30 || it in 100..110 }
        assertThat(injected.publicMultipleMeansInt).matches { abs(abs(it) - 100) <= 30 }
        assertThat(injected.publicMultipleRangesLong).matches { it in 20L..30L || it in 100L..110L }
        assertThat(injected.publicMultipleMeansLong).matches { abs(abs(it) - 100L) <= 30L }
        assertThat(injected.publicMultipleRangesFloat).matches { it in 20f..30f || it in 100f..110f }
        assertThat(injected.publicMultipleMeansFloat).matches { abs(abs(it) - 100f) <= 30f }
        assertThat(injected.publicMultipleRangesDouble).matches { it in 20.0..30.0 || it in 100.0..110.0 }
        assertThat(injected.publicMultipleMeansDouble).matches { abs(abs(it) - 100.0) <= 30.0 }
        assertThat(injected.publicGeneric).isNotNull()
    }

    @Test
    fun injectMap() {
        val injected = KotlinInjectedMap()

        injector.inject(forge, injected, null)

        assertThat(injected.publicMap.entries)
            .isNotEmpty()
            .allMatch {
                it.key.matches(Regex("[a-fA-F0-9]+")) && it.value > 0L
            }
        assertThat(injected.publicFooMap.entries)
            .isNotEmpty()
            .allMatch {
                it.key.matches(Regex("\\w+@\\w+\\.[a-z]{3}")) && it.value != null
            }
        assertThat(injected.publicNestedFooMap.entries)
            .isNotEmpty()
            .allMatch {
                it.key.matches(Regex("[0-9]+")) && it.value.isNotEmpty() && it.value.entries.all { nested ->
                    nested.key >= -100 && nested.key <= 100 && nested.value != null
                }
            }
    }
}

private fun <
    SELF : AbstractAssert<SELF, ACTUAL>,
    ACTUAL : Collection<T>,
    T : Comparable<T>
    > AbstractAssert<SELF, ACTUAL>.hasDistinctValues(minLength: Int = 4): SELF {
    return matches {
        if (it.size > minLength) {
            it.sorted().distinct().size > 1
        } else {
            true
        }
    }
}
