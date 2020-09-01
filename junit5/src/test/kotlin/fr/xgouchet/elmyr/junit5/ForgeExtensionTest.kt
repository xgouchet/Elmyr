package fr.xgouchet.elmyr.junit5

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactoryMissingException
import fr.xgouchet.elmyr.annotation.BoolForgery
import fr.xgouchet.elmyr.annotation.DoubleForgery
import fr.xgouchet.elmyr.annotation.FloatForgery
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.annotation.LongForgery
import fr.xgouchet.elmyr.annotation.RegexForgery
import fr.xgouchet.elmyr.annotation.StringForgery
import fr.xgouchet.elmyr.junit5.dummy.Foo
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.PrintStream
import java.lang.reflect.Method
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
internal class ForgeExtensionTest {

    lateinit var testedExtension: ForgeExtension

    @Mock
    lateinit var mockExtensionContext: ExtensionContext

    @Mock
    lateinit var mockParameterContext: ParameterContext

    @Mock
    lateinit var mockTarget: Any

    lateinit var fakeMethod: Method

    private lateinit var originalStream: PrintStream
    private lateinit var errStreamContent: ByteArrayOutputStream

    @BeforeEach
    fun setUp() {
        testedExtension = ForgeExtension()

        fakeMethod = String::class.java.methods.first()

        originalStream = System.err
        errStreamContent = ByteArrayOutputStream()
        System.setErr(PrintStream(errStreamContent))
    }

    @AfterEach
    fun tearDown() {
        System.setErr(originalStream)
    }

    // region ParameterResolver.supportsParameter

    @Test
    fun `supportsParameter with Forgery`() {
        prepareContext("withStringForgery")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with BoolForgery`() {
        prepareContext("withBool")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with BoolForgery {List}`() {
        prepareContext("withBoolList")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with BoolForgery {Set}`() {
        prepareContext("withBoolSet")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with BoolForgery {Collection}`() {
        prepareContext("withBoolCollection")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with BoolForgery {NestedList}`() {
        prepareContext("withBoolNestedList")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with IntForgery`() {
        prepareContext("withInt")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with IntForgery {List}`() {
        prepareContext("withIntList")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with IntForgery {Set}`() {
        prepareContext("withIntSet")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with IntForgery {Collection}`() {
        prepareContext("withIntCollection")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with IntForgery {NestedList}`() {
        prepareContext("withIntNestedList")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with LongForgery`() {
        prepareContext("withLong")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with LongForgery {List}`() {
        prepareContext("withLongList")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with LongForgery {Set}`() {
        prepareContext("withLongSet")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with LongForgery {Collection}`() {
        prepareContext("withLongCollection")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with LongForgery {NestedList}`() {
        prepareContext("withLongNestedList")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with FloatForgery`() {
        prepareContext("withFloat")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with FloatForgery {List}`() {
        prepareContext("withFloatList")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with FloatForgery {Set}`() {
        prepareContext("withFloatSet")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with FloatForgery {Collection}`() {
        prepareContext("withFloatCollection")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with FloatForgery {NestedList}`() {
        prepareContext("withFloatNestedList")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with DoubleForgery`() {
        prepareContext("withDouble")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with DoubleForgery {List}`() {
        prepareContext("withDoubleList")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with DoubleForgery {Set}`() {
        prepareContext("withDoubleSet")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with DoubleForgery {Collection}`() {
        prepareContext("withDoubleCollection")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with DoubleForgery {NestedList}`() {
        prepareContext("withDoubleNestedList")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with StringForgery`() {
        prepareContext("withString")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with StringForgery {List}`() {
        prepareContext("withStringList")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with StringForgery {Set}`() {
        prepareContext("withStringSet")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with StringForgery {Collection}`() {
        prepareContext("withStringCollection")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with StringForgery {NestedList}`() {
        prepareContext("withStringNestedList")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with RegexForgery`() {
        prepareContext("withRegex")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with RegexForgery {List}`() {
        prepareContext("withRegexList")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with RegexForgery {Set}`() {
        prepareContext("withRegexSet")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with RegexForgery {Collection}`() {
        prepareContext("withRegexCollection")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter with RegexForgery {NestedList}`() {
        prepareContext("withRegexNestedList")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter returns true on un-annotated and Forge type`() {
        prepareContext("withForge")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter Fails on Constructor with Forgery`() {
        val fakeConstructor = Reflekta::class.java.constructors.first()
        whenever(mockParameterContext.isAnnotated(Forgery::class.java)) doReturn true
        whenever(mockParameterContext.declaringExecutable) doReturn fakeConstructor

        assertThrows<IllegalStateException> {
            testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `supportsParameter returns false on un-annotated`() {
        prepareContext("withUnannotatedString")

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isFalse()
    }

    @Test
    fun `supportsParameter Fails on BoolForgery param not Bool`() {
        prepareContext("withNotBool")

        assertThrows<IllegalStateException> {
            testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `supportsParameter Fails on IntForgery param not Int`() {
        prepareContext("withNotInt")

        assertThrows<IllegalStateException> {
            testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `supportsParameter Fails on LongForgery param not Long`() {
        prepareContext("withNotLong")

        assertThrows<IllegalStateException> {
            testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `supportsParameter Fails on FloatForgery param not Float`() {
        prepareContext("withNotFloat")

        assertThrows<IllegalStateException> {
            testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `supportsParameter Fails on DoubleForgery param not Double`() {
        prepareContext("withNotDouble")

        assertThrows<IllegalStateException> {
            testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `supportsParameter Fails on StringForgery param not String`() {
        prepareContext("withNotString")

        assertThrows<IllegalStateException> {
            testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `supportsParameter Fails on RegexForgery param not Regex`() {
        prepareContext("withNotRegex")

        assertThrows<IllegalStateException> {
            testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)
        }
    }

    // endregion

    // region ParameterResolver.resolveParameter

    @Test
    fun `resolveParameter {Forge}`() {
        prepareContext("withForge")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isSameAs(testedExtension.instanceForge)
    }

    @Test
    fun `resolveParameter {Bool}`() {
        prepareContext("withBool")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).matches { it is Boolean }
    }

    @Test
    fun `resolveParameter {Bool list}`() {
        prepareContext("withBoolList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).isNotEmpty().allMatch { it is Boolean }
    }

    @Test
    fun `resolveParameter {Bool set}`() {
        prepareContext("withBoolSet")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Set<*>).isNotEmpty().allMatch { it is Boolean }
    }

    @Test
    fun `resolveParameter {Bool collection}`() {
        prepareContext("withBoolCollection")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Collection<*>).isNotEmpty().allMatch { it is Boolean }
    }

    @Test
    fun `resolveParameter {Bool nested list}`() {
        prepareContext("withBoolNestedList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).allMatch { it is Set<*> && it.all { b -> b is Boolean } }
    }

    @Test
    fun `resolveParameter {Int}`() {
        prepareContext("withInt")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).matches { it is Int }
    }

    @Test
    fun `resolveParameter {Int list}`() {
        prepareContext("withIntList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).isNotEmpty().allMatch { it is Int }
    }

    @Test
    fun `resolveParameter {Int set}`() {
        prepareContext("withIntSet")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Set<*>).isNotEmpty().allMatch { it is Int }
    }

    @Test
    fun `resolveParameter {Int collection}`() {
        prepareContext("withIntCollection")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Collection<*>).isNotEmpty().allMatch { it is Int }
    }

    @Test
    fun `resolveParameter {Int nested list}`() {
        prepareContext("withIntNestedList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).allMatch { it is Set<*> && it.all { b -> b is Int } }
    }

    @Test
    fun `resolveParameter {Long}`() {
        prepareContext("withLong")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).matches { it is Long }
    }

    @Test
    fun `resolveParameter {Long list}`() {
        prepareContext("withLongList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).isNotEmpty().allMatch { it is Long }
    }

    @Test
    fun `resolveParameter {Long set}`() {
        prepareContext("withLongSet")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Set<*>).isNotEmpty().allMatch { it is Long }
    }

    @Test
    fun `resolveParameter {Long collection}`() {
        prepareContext("withLongCollection")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Collection<*>).isNotEmpty().allMatch { it is Long }
    }

    @Test
    fun `resolveParameter {Long nested list}`() {
        prepareContext("withLongNestedList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).allMatch { it is Set<*> && it.all { b -> b is Long } }
    }

    @Test
    fun `resolveParameter {Float}`() {
        prepareContext("withFloat")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).matches { it is Float }
    }

    @Test
    fun `resolveParameter {Float list}`() {
        prepareContext("withFloatList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).isNotEmpty().allMatch { it is Float }
    }

    @Test
    fun `resolveParameter {Float set}`() {
        prepareContext("withFloatSet")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Set<*>).isNotEmpty().allMatch { it is Float }
    }

    @Test
    fun `resolveParameter {Float collection}`() {
        prepareContext("withFloatCollection")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Collection<*>).isNotEmpty().allMatch { it is Float }
    }

    @Test
    fun `resolveParameter {Float nested list}`() {
        prepareContext("withFloatNestedList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).allMatch { it is Set<*> && it.all { b -> b is Float } }
    }

    @Test
    fun `resolveParameter {Double}`() {
        prepareContext("withDouble")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).matches { it is Double }
    }

    @Test
    fun `resolveParameter {Double list}`() {
        prepareContext("withDoubleList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).isNotEmpty().allMatch { it is Double }
    }

    @Test
    fun `resolveParameter {Double set}`() {
        prepareContext("withDoubleSet")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Set<*>).isNotEmpty().allMatch { it is Double }
    }

    @Test
    fun `resolveParameter {Double collection}`() {
        prepareContext("withDoubleCollection")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Collection<*>).isNotEmpty().allMatch { it is Double }
    }

    @Test
    fun `resolveParameter {Double nested list}`() {
        prepareContext("withDoubleNestedList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).allMatch { it is Set<*> && it.all { b -> b is Double } }
    }

    @Test
    fun `resolveParameter {String}`() {
        prepareContext("withString")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).matches { it is String }
    }

    @Test
    fun `resolveParameter {String list}`() {
        prepareContext("withStringList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).isNotEmpty().allMatch { it is String }
    }

    @Test
    fun `resolveParameter {String set}`() {
        prepareContext("withStringSet")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Set<*>).isNotEmpty().allMatch { it is String }
    }

    @Test
    fun `resolveParameter {String collection}`() {
        prepareContext("withStringCollection")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Collection<*>).isNotEmpty().allMatch { it is String }
    }

    @Test
    fun `resolveParameter {String nested list}`() {
        prepareContext("withStringNestedList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).allMatch { it is Set<*> && it.all { b -> b is String } }
    }

    @Test
    fun `resolveParameter {StringRegex}`() {
        prepareContext("withStringRegex")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).matches { it is String && it.matches(Regex("[abc]+")) }
    }

    @Test
    fun `resolveParameter {StringRegex list}`() {
        prepareContext("withStringRegexList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).isNotEmpty().allMatch { it is String && it.matches(Regex("[abc]+")) }
    }

    @Test
    fun `resolveParameter {StringRegex set}`() {
        prepareContext("withStringRegexSet")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Set<*>).isNotEmpty().allMatch { it is String && it.matches(Regex("[abc]+")) }
    }

    @Test
    fun `resolveParameter {StringRegex collection}`() {
        prepareContext("withStringRegexCollection")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Collection<*>).isNotEmpty().allMatch { it is String && it.matches(Regex("[abc]+")) }
    }

    @Test
    fun `resolveParameter {StringRegex nested list}`() {
        prepareContext("withStringRegexNestedList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).allMatch {
            it is Set<*> && it.all { b -> b is String && b.matches(Regex("[abc]+")) }
        }
    }

    @Test
    fun `resolveParameter {Regex}`() {
        prepareContext("withRegex")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).matches { it is String && it.matches(Regex("[abc]+")) }
    }

    @Test
    fun `resolveParameter {Regex list}`() {
        prepareContext("withRegexList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).isNotEmpty().allMatch { it is String && it.matches(Regex("[abc]+")) }
    }

    @Test
    fun `resolveParameter {Regex set}`() {
        prepareContext("withRegexSet")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Set<*>).isNotEmpty().allMatch { it is String && it.matches(Regex("[abc]+")) }
    }

    @Test
    fun `resolveParameter {Regex collection}`() {
        prepareContext("withRegexCollection")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as Collection<*>).isNotEmpty().allMatch { it is String && it.matches(Regex("[abc]+")) }
    }

    @Test
    fun `resolveParameter {Regex nested list}`() {
        prepareContext("withRegexNestedList")

        val result = testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)

        assertThat(result as List<*>).allMatch {
            it is Set<*> && it.all { b -> b is String && b.matches(Regex("[abc]+")) }
        }
    }

    // endregion

    // region ParameterResolver.resolveParameter (failing)

    @Test
    fun `resolveParameter Fails on unknown parameterized type`() {
        prepareContext("withParameterized")

        assertThrows<ForgeryFactoryMissingException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on BoolForgery param invalid probability lt 0`() {
        prepareContext("withBoolInvalid1")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on BoolForgery param invalid probability gt 1`() {
        prepareContext("withBoolInvalid2")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on IntForgery param invalid min + stDev`() {
        prepareContext("withIntInvalid1")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on IntForgery param invalid max + stDev`() {
        prepareContext("withIntInvalid2")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on IntForgery param invalid min + mean`() {
        prepareContext("withIntInvalid3")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on IntForgery param invalid max + mean`() {
        prepareContext("withIntInvalid4")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on LongForgery param invalid min + stDev`() {
        prepareContext("withLongInvalid1")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on LongForgery param invalid max + stDev`() {
        prepareContext("withLongInvalid2")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on LongForgery param invalid min + mean`() {
        prepareContext("withLongInvalid3")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on LongForgery param invalid max + mean`() {
        prepareContext("withLongInvalid4")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on FloatForgery param invalid min + stDev`() {
        prepareContext("withFloatInvalid1")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on FloatForgery param invalid max + stDev`() {
        prepareContext("withFloatInvalid2")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on FloatForgery param invalid min + mean`() {
        prepareContext("withFloatInvalid3")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on FloatForgery param invalid max + mean`() {
        prepareContext("withFloatInvalid4")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on DoubleForgery param invalid min + stDev`() {
        prepareContext("withDoubleInvalid1")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on DoubleForgery param invalid max + stDev`() {
        prepareContext("withDoubleInvalid2")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on DoubleForgery param invalid min + mean`() {
        prepareContext("withDoubleInvalid3")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `resolveParameter Fails on DoubleForgery param invalid max + mean`() {
        prepareContext("withDoubleInvalid4")
        assertThrows<IllegalStateException> {
            testedExtension.resolveParameter(mockParameterContext, mockExtensionContext)
        }
    }

    // endregion

    // region TestExecutionExceptionHandler

    @Test
    fun handleException() {
        whenever(mockExtensionContext.requiredTestInstance) doReturn mockTarget
        whenever(mockExtensionContext.requiredTestMethod) doReturn fakeMethod
        val thrown: Throwable = IOException("Whatever")

        val caught: Throwable? = try {
            testedExtension.handleTestExecutionException(
                mockExtensionContext,
                thrown
            )
            null
        } catch (e: IOException) {
            e
        }

        assertThat(caught).isSameAs(thrown)

        val forge = testedExtension.instanceForge
        assertThat(errStreamContent.toString())
            .isEqualTo(
                "<${mockTarget.javaClass.simpleName}.${fakeMethod.name}()> failed " +
                    "with Forge seed 0x${forge.seed.toString(16)}L\n" +
                    "Add the following @ForgeConfiguration annotation to your test class :\n" +
                    "\n" +
                    "\t@ForgeConfiguration(seed = 0x${forge.seed.toString(16)}L)\n" +
                    "\n"
            )
    }

    // endregion

    // region Internal

    fun prepareContext(methodName: String, parameterIndex: Int = 0) {
        val javaClass = Reflekta::class.java
        val method = javaClass.declaredMethods.first { it.name == methodName }
        val parameter = method.parameters[parameterIndex]
        mockParameterContext = MockParameterContext(javaClass, method, parameter)
    }

    // endregion
}

@Suppress("unused", "UNUSED_PARAMETER")
internal class Reflekta(@Forgery s: String) {

    fun withStringForgery(@Forgery s: String) {
    }

    fun withUnannotatedString(s: String) {
    }

    fun withForge(f: Forge) {
    }

    // region boolean

    fun withBool(@BoolForgery b: Boolean) {
    }

    fun withNotBool(@BoolForgery s: String) {
    }

    fun withBoolInvalid1(@BoolForgery(probability = -1f) b: Boolean) {
    }

    fun withBoolInvalid2(@BoolForgery(probability = 2f) b: Boolean) {
    }

    fun withBoolList(@BoolForgery b: List<Boolean>) {
    }

    fun withBoolSet(@BoolForgery b: Set<Boolean>) {
    }

    fun withBoolCollection(@BoolForgery b: Collection<Boolean>) {
    }

    fun withBoolNestedList(@BoolForgery b: List<Set<Boolean>>) {
    }

    // endregion 

    // region int

    fun withInt(@IntForgery i: Int) {
    }

    fun withNotInt(@IntForgery s: String) {
    }

    fun withIntInvalid1(@IntForgery(min = 0, standardDeviation = 0) i: Int) {
    }

    fun withIntInvalid2(@IntForgery(max = 0, standardDeviation = 0) i: Int) {
    }

    fun withIntInvalid3(@IntForgery(min = 0, mean = 10) i: Int) {
    }

    fun withIntInvalid4(@IntForgery(max = 0, mean = 10) i: Int) {
    }

    fun withIntList(@IntForgery b: List<Int>) {
    }

    fun withIntSet(@IntForgery b: Set<Int>) {
    }

    fun withIntCollection(@IntForgery b: Collection<Int>) {
    }

    fun withIntNestedList(@IntForgery b: List<Set<Int>>) {
    }

    // endregion 

    // region long

    fun withLong(@LongForgery i: Long) {
    }

    fun withNotLong(@LongForgery s: String) {
    }

    fun withLongInvalid1(@LongForgery(min = 0L, standardDeviation = 0L) l: Long) {
    }

    fun withLongInvalid2(@LongForgery(max = 0L, standardDeviation = 0L) l: Long) {
    }

    fun withLongInvalid3(@LongForgery(min = 0L, mean = 10L) l: Long) {
    }

    fun withLongInvalid4(@LongForgery(max = 0L, mean = 10L) l: Long) {
    }

    fun withLongList(@LongForgery b: List<Long>) {
    }

    fun withLongSet(@LongForgery b: Set<Long>) {
    }

    fun withLongCollection(@LongForgery b: Collection<Long>) {
    }

    fun withLongNestedList(@LongForgery b: List<Set<Long>>) {
    }

    // endregion 

    // region float

    fun withFloat(@FloatForgery i: Float) {
    }

    fun withNotFloat(@FloatForgery s: String) {
    }

    fun withFloatInvalid1(@FloatForgery(min = 0f, standardDeviation = 0f) f: Float) {
    }

    fun withFloatInvalid2(@FloatForgery(max = 0f, standardDeviation = 0f) f: Float) {
    }

    fun withFloatInvalid3(@FloatForgery(min = 0f, mean = 10F) f: Float) {
    }

    fun withFloatInvalid4(@FloatForgery(max = 0f, mean = 10F) f: Float) {
    }

    fun withFloatList(@FloatForgery b: List<Float>) {
    }

    fun withFloatSet(@FloatForgery b: Set<Float>) {
    }

    fun withFloatCollection(@FloatForgery b: Collection<Float>) {
    }

    fun withFloatNestedList(@FloatForgery b: List<Set<Float>>) {
    }

    // endregion 

    // region double

    fun withDouble(@DoubleForgery i: Double) {
    }

    fun withNotDouble(@DoubleForgery s: String) {
    }

    fun withDoubleInvalid1(@DoubleForgery(min = 0.0, standardDeviation = 0.0) d: Double) {
    }

    fun withDoubleInvalid2(@DoubleForgery(max = 0.0, standardDeviation = 0.0) d: Double) {
    }

    fun withDoubleInvalid3(@DoubleForgery(min = 0.0, mean = 10.0) d: Double) {
    }

    fun withDoubleInvalid4(@DoubleForgery(max = 0.0, mean = 10.0) d: Double) {
    }

    fun withDoubleList(@DoubleForgery b: List<Double>) {
    }

    fun withDoubleSet(@DoubleForgery b: Set<Double>) {
    }

    fun withDoubleCollection(@DoubleForgery b: Collection<Double>) {
    }

    fun withDoubleNestedList(@DoubleForgery b: List<Set<Double>>) {
    }

    // endregion

    // region string

    fun withString(@StringForgery s: String) {
    }

    fun withNotString(@StringForgery b: Boolean) {
    }

    fun withStringList(@StringForgery b: List<String>) {
    }

    fun withStringSet(@StringForgery b: Set<String>) {
    }

    fun withStringCollection(@StringForgery b: Collection<String>) {
    }

    fun withStringNestedList(@StringForgery b: List<Set<String>>) {
    }

    fun withStringRegex(@StringForgery(regex = "[abc]+") s: String) {
    }

    fun withNotStringRegex(@StringForgery(regex = "[abc]+") f: Float) {
    }

    fun withStringRegexList(@StringForgery(regex = "[abc]+") b: List<String>) {
    }

    fun withStringRegexSet(@StringForgery(regex = "[abc]+") b: Set<String>) {
    }

    fun withStringRegexCollection(@StringForgery(regex = "[abc]+") b: Collection<String>) {
    }

    fun withStringRegexNestedList(@StringForgery(regex = "[abc]+") b: List<Set<String>>) {
    }

    // endregion

    // region Regex

    fun withRegex(@RegexForgery("[a-c]+") s: String) {
    }

    fun withNotRegex(@RegexForgery("[a-c]+") f: Float) {
    }

    fun withRegexList(@RegexForgery("[a-c]+") b: List<String>) {
    }

    fun withRegexSet(@RegexForgery("[a-c]+") b: Set<String>) {
    }

    fun withRegexCollection(@RegexForgery("[a-c]+") b: Collection<String>) {
    }

    fun withRegexNestedList(@RegexForgery("[a-c]+") b: List<Set<String>>) {
    }

    // endregion

    fun withParameterized(@Forgery c: Comparator<Foo>) {
    }
}
