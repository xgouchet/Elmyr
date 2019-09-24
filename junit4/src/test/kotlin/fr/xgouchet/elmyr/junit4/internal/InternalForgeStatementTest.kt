package fr.xgouchet.elmyr.junit4.internal

import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.whenever
import fr.xgouchet.elmyr.junit4.JUnitForge
import org.assertj.core.api.Assertions.assertThat
import org.junit.AssumptionViolatedException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class InternalForgeStatementTest {

    @Rule @JvmField val forge = JUnitForge()
    @Rule @JvmField val mockito = MockitoJUnit.rule().strictness(Strictness.LENIENT)

    internal lateinit var testedStatement: InternalForgeStatement

    @Mock lateinit var mockBaseStatement: Statement
    @Mock lateinit var mockMethod: FrameworkMethod
    @Mock lateinit var mockTarget: Any

    private lateinit var errStreamContent: ByteArrayOutputStream

    @Before
    fun setUp() {
        errStreamContent = ByteArrayOutputStream()
        System.setErr(PrintStream(errStreamContent))

        testedStatement = InternalForgeStatement(
                mockBaseStatement,
                mockMethod,
                mockTarget,
                forge
        )
    }

    @Test
    fun `prints seed when failure happens`() {
        whenever(mockBaseStatement.evaluate()) doThrow AssertionError()

        val e: AssertionError? = try {
            testedStatement.evaluate()
            null
        } catch (e: AssertionError) {
            e
        }

        assertThat(e).isNotNull()

        assertThat(errStreamContent.toString())
                .isEqualTo(
                        "<${mockTarget.javaClass.simpleName}.${mockMethod.name}()> failed " +
                                "with Forge seed 0x${forge.seed.toString(16)}\n" +
                                "Add the following line in your @Before method to reproduce :\n" +
                                "\n" +
                                "\tforger.resetSeed(0x${forge.seed.toString(16)}L)\n" +
                                "\n")
    }

    @Test
    fun `ignores skipped test`() {
        whenever(mockBaseStatement.evaluate()) doThrow AssumptionViolatedException("")

        val e: AssumptionViolatedException? = try {
            testedStatement.evaluate()
            null
        } catch (e: AssumptionViolatedException) {
            e
        }

        assertThat(e).isNotNull()

        assertThat(errStreamContent.toString())
                .isEmpty()
    }
}