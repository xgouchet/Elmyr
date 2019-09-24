package fr.xgouchet.elmyr.junit5

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.PrintStream
import java.lang.reflect.Method

@ExtendWith(MockitoExtension::class)
class ForgeExtensionTest {

    lateinit var testedExtension: ForgeExtension

    @Mock lateinit var mockContext: ExtensionContext
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
    fun handleException() {
        whenever(mockContext.requiredTestInstance) doReturn mockTarget
        whenever(mockContext.requiredTestMethod) doReturn fakeMethod
        val thrown: Throwable = IOException("Whatever")

        val caught: Throwable? = try {
            testedExtension.handleTestExecutionException(
                    mockContext,
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
}