package fr.xgouchet.elmyr.junit4

import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.AssumptionViolatedException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.contrib.java.lang.system.SystemErrRule
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness

class ForgeStatementTest {

    @Rule @JvmField val forge = ForgeRule()
    @Rule @JvmField val mockito = MockitoJUnit.rule().strictness(Strictness.LENIENT)
    @Rule @JvmField val systemErrRule = SystemErrRule().enableLog()

    internal lateinit var testedStatement: ForgeStatement

    @Mock lateinit var mockBaseStatement: Statement
    @Mock lateinit var mockMethod: FrameworkMethod
    @Mock lateinit var mockTarget: Any

    @Before
    fun setUp() {
        testedStatement = ForgeStatement(
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

        assertThat(systemErrRule.log)
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

        assertThat(systemErrRule.log)
                .isEmpty()
    }
}
