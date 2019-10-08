package fr.xgouchet.elmyr.junit5

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.Forgery
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolutionException
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.PrintStream
import java.lang.reflect.Method
import java.lang.reflect.Parameter

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ForgeExtensionTest {

    lateinit var testedExtension: ForgeExtension

    @Mock lateinit var mockExtensionContext: ExtensionContext
    @Mock lateinit var mockParameterContext: ParameterContext
    @Mock lateinit var mockTarget: Any

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

    @Test
    fun applyConfiguration() {
        var confApplied = false
        var fakeInt = 0

        testedExtension.configure {
            confApplied = true
            fakeInt = anInt(3, 42)
        }

        assertThat(confApplied).isTrue()
        assertThat(fakeInt).isBetween(3, 42)
    }

    // region ParameterResolver

    @Test
    fun supportsParameter() {
        val fakeParameter: Parameter = Reflekta::class.java.declaredMethods
                .filter { it.parameterCount > 0 }
                .map { it.parameters.first() }
                .first()
        whenever(mockParameterContext.isAnnotated(Forgery::class.java)) doReturn true
        whenever(mockParameterContext.declaringExecutable) doReturn mock()
        whenever(mockParameterContext.parameter) doReturn fakeParameter

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter returns true on un-annotated and Forge type`() {
        val fakeParameter: Parameter = Reflekta::class.java.declaredMethods
                .filter { it.parameterCount > 0 }
                .mapNotNull { method ->
                    method.parameters.firstOrNull { param ->
                        param.type == Forge::class.java
                    }
                }
                .first()
        whenever(mockParameterContext.isAnnotated(Forgery::class.java)) doReturn false
        whenever(mockParameterContext.declaringExecutable) doReturn mock()
        whenever(mockParameterContext.parameter) doReturn fakeParameter

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `supportsParameter Fails on Constructor`() {
        val fakeConstructor = Reflekta::class.java.constructors.first()
        whenever(mockParameterContext.isAnnotated(Forgery::class.java)) doReturn true
        whenever(mockParameterContext.declaringExecutable) doReturn fakeConstructor

        assertThrows<ParameterResolutionException> {
            testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)
        }
    }

    @Test
    fun `supportsParameter returns false on un-annotated`() {
        val fakeParameter: Parameter = Reflekta::class.java.declaredMethods
                .filter { it.parameterCount > 0 }
                .mapNotNull { method -> method.parameters.firstOrNull { it.type != Forge::class.java } }
                .first()
        whenever(mockParameterContext.isAnnotated(Forgery::class.java)) doReturn false
        whenever(mockParameterContext.parameter) doReturn fakeParameter

        val result = testedExtension.supportsParameter(mockParameterContext, mockExtensionContext)

        assertThat(result).isFalse()
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
                                "with Forge seed 0x${forge.seed.toString(16)}\n" +
                                "Add the following line in your @Before method to reproduce :\n" +
                                "\n" +
                                "\tforger.resetSeed(0x${forge.seed.toString(16)}L)\n" +
                                "\n")
    }

    // endregion

    // region Reflect Internal
}

class Reflekta(s: String) {

    fun method(@Forgery s: String) {
    }

    fun methodWithForge(f: Forge) {
    }
}