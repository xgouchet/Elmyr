package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.Case
import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeConfigurator
import fr.xgouchet.elmyr.annotation.BoolForgery
import fr.xgouchet.elmyr.annotation.DoubleForgery
import fr.xgouchet.elmyr.annotation.FloatForgery
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.annotation.LongForgery
import fr.xgouchet.elmyr.annotation.RegexForgery
import fr.xgouchet.elmyr.annotation.StringForgery
import fr.xgouchet.elmyr.junit5.dummy.Bar
import fr.xgouchet.elmyr.junit5.dummy.BarFactory
import fr.xgouchet.elmyr.junit5.dummy.Foo
import fr.xgouchet.elmyr.junit5.dummy.FooFactory
import java.time.Month
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(KotlinAnnotationTest.Configurator::class)
open class KotlinAnnotationTest {

    @Forgery
    lateinit var fakeFoo: Foo

    @Forgery
    lateinit var fakeFooList: List<Foo>

    @Forgery
    lateinit var fakeFooSet: Set<Foo>

    @Forgery
    lateinit var fakeFooMap: Map<Foo, Bar>

    @Forgery
    lateinit var fakeMonth: Month

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
    fun injectIntWithDefaultProbability(@BoolForgery b: Boolean) {
        checkForgeryInjectedInField()
    }

    @Test
    fun injectIntWithCustomProbability(@BoolForgery(probability = 0.001f) b: Boolean) {
        checkForgeryInjectedInField()
    }

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

    // region Strings

    @Test
    fun injectStringWithRegex(@RegexForgery(TEST_REGEX) s: String) {
        assertThat(s).matches(TEST_REGEX)
        checkForgeryInjectedInField()
    }

    @Test
    fun injectAsciiString(@StringForgery(StringForgery.Type.ASCII) s: String) {
        s.forEach {
            assertThat(it)
                    .isGreaterThanOrEqualTo(MIN_PRINTABLE)
                    .isLessThanOrEqualTo(MAX_ASCII)
        }
        checkForgeryInjectedInField()
    }

    @Test
    fun injectAsciiExtendedString(@StringForgery(StringForgery.Type.ASCII_EXTENDED) s: String) {
        s.forEach {
            assertThat(it)
                    .isGreaterThanOrEqualTo(MIN_PRINTABLE)
                    .isLessThanOrEqualTo(MAX_ASCII_EXTENDED)
        }
        checkForgeryInjectedInField()
    }

    // TODO add case

    @Test
    fun injectAlphabeticalString(@StringForgery(StringForgery.Type.ALPHABETICAL) s: String) {
        assertThat(s).matches("[a-zA-Z]+")
        checkForgeryInjectedInField()
    }

    @Test
    fun injectAlphabeticalLowercaseString(
        @StringForgery(StringForgery.Type.ALPHABETICAL, Case.LOWER) s: String
    ) {
        assertThat(s).matches("[a-z]+")
        checkForgeryInjectedInField()
    }

    @Test
    fun injectAlphabeticalUppercaseString(
        @StringForgery(StringForgery.Type.ALPHABETICAL, Case.UPPER) s: String
    ) {
        assertThat(s).matches("[A-Z]+")
        checkForgeryInjectedInField()
    }

    @Test
    fun injectAlphaNumericalString(@StringForgery(StringForgery.Type.ALPHA_NUMERICAL) s: String) {
        assertThat(s).matches("[a-zA-Z0-9]+")
        checkForgeryInjectedInField()
    }

    @Test
    fun injectAlphaNumericalLowercaseString(
        @StringForgery(StringForgery.Type.ALPHA_NUMERICAL, Case.LOWER) s: String
    ) {
        assertThat(s).matches("[a-z0-9]+")
        checkForgeryInjectedInField()
    }

    @Test
    fun injectAlphaNumericalUpercaseString(
        @StringForgery(StringForgery.Type.ALPHA_NUMERICAL, Case.UPPER) s: String
    ) {
        assertThat(s).matches("[A-Z0-9]+")
        checkForgeryInjectedInField()
    }

    @Test
    fun injectWhitespaceString(@StringForgery(StringForgery.Type.WHITESPACE) s: String) {
        assertThat(s).matches("\\s+")
        checkForgeryInjectedInField()
    }

    @Test
    fun injectHexadecimalString(@StringForgery(StringForgery.Type.HEXADECIMAL) s: String) {
        assertThat(s).matches("[a-fA-F0-9]+")
        checkForgeryInjectedInField()
    }

    @Test
    fun injectHexadecimalLowercaseString(
        @StringForgery(StringForgery.Type.HEXADECIMAL, Case.LOWER) s: String
    ) {
        assertThat(s).matches("[a-f0-9]+")
        checkForgeryInjectedInField()
    }

    @Test
    fun injectHexadecimalUppercaseString(
        @StringForgery(StringForgery.Type.HEXADECIMAL, Case.UPPER) s: String
    ) {
        assertThat(s).matches("[A-F0-9]+")
        checkForgeryInjectedInField()
    }

    @Test
    fun injectNumericalString(@StringForgery(StringForgery.Type.NUMERICAL) s: String) {
        assertThat(s).matches("[0-9]+")
        checkForgeryInjectedInField()
    }

    // endregion

    // region Enum

    @Test
    fun injectEnumForgery(@Forgery month: Month) {
        assertThat(month)
                .isNotNull()
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
        assertThat(list).isNotNull.isNotEmpty

        list.forEach {
            assertThat(it).isInstanceOf(Foo::class.java)
        }
    }

    @Test
    fun injectCollection(@Forgery collection: Collection<Foo>) {
        assertThat(collection).isNotNull.isNotEmpty

        collection.forEach {
            assertThat(it).isInstanceOf(Foo::class.java)
        }
    }

    @Test
    fun injectList(@Forgery list: List<Foo>) {
        assertThat(list).isNotNull.isNotEmpty

        list.forEach {
            assertThat(it).isInstanceOf(Foo::class.java)
        }
    }

    @Test
    fun injectSet(@Forgery set: Set<Foo>) {
        assertThat(set).isNotNull.isNotEmpty

        set.forEach {
            assertThat(it).isInstanceOf(Foo::class.java)
        }
    }

    @Test
    fun injectNestedCollection(@Forgery nested: List<Set<Foo>>) {
        assertThat(nested).isNotNull.isNotEmpty

        nested.forEach { set ->
            assertThat(set).isNotNull.isNotEmpty
            set.forEach {
                assertThat(it).isInstanceOf(Foo::class.java)
            }
        }
    }

    @Test
    fun injectMap(@Forgery map: Map<Foo, Bar>) {
        assertThat(map).isNotNull.isNotEmpty

        map.forEach {
            assertThat(it.key).isInstanceOf(Foo::class.java)
            assertThat(it.value).isInstanceOf(Bar::class.java)
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
            assertThat(fakeFoo).isNotEqualTo(previousFoo)
        }
        memoizedFoo = fakeFoo

        assertThat(fakeFoo).isNotNull()
        assertThat(fakeFooList).isNotNull.isNotEmpty
        assertThat(fakeFooSet).isNotNull.isNotEmpty
        assertThat(fakeFooMap).isNotNull.isNotEmpty

        assertThat(fakeMonth).isNotNull()
    }

    // endregion

    class Configurator : ForgeConfigurator {
        override fun configure(forge: Forge) {
            forge.addFactory(Foo::class.java, FooFactory())
            forge.addFactory(Bar::class.java, BarFactory())
        }
    }

    companion object {
        internal var memoizedSeed: Long? = null
        internal var memoizedFoo: Foo? = null

        internal const val MIN_PRINTABLE = 0x20.toChar()
        internal const val MAX_ASCII = 0x7F.toChar()
        internal const val MAX_ASCII_EXTENDED = 0xFF.toChar()

        internal const val TEST_REGEX = "[a-z](\\d+)[_\\.\\-][A-Z]*"
    }
}
