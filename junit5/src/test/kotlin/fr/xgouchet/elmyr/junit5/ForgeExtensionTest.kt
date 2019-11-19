package fr.xgouchet.elmyr.junit5

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactoryMissingException
import fr.xgouchet.elmyr.annotation.DoubleForgery
import fr.xgouchet.elmyr.annotation.FloatForgery
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.annotation.LongForgery
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
import kotlin.Comparator

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ForgeExtensionTest {

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
    fun `supportsParameter with IntForgery`() {
        prepareContext("withInt")

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
    fun `supportsParameter with FloatForgery`() {
        prepareContext("withFloat")

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

    // endregion

    // region ParameterResolver.resolveParameter

    @Test
    fun `resolveParameter Fails on unknown parameterized type`() {
        prepareContext("withParameterized")

        assertThrows<ForgeryFactoryMissingException> {
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
                                "Add the following line in your @BeforeEach method to reproduce :\n" +
                                "\n" +
                                "\tforge.setSeed(0x${forge.seed.toString(16)}L);\n" +
                                "\n")
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
class Reflekta(@Forgery s: String) {

    fun withStringForgery(@Forgery s: String) {
    }

    fun withUnannotatedString(s: String) {
    }

    fun withForge(f: Forge) {
    }

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

    // endregion 

    fun withParameterized(@Forgery c: Comparator<Foo>) {
    }
}
