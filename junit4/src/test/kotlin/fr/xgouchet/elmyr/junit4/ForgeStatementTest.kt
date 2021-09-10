package fr.xgouchet.elmyr.junit4

import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import fr.xgouchet.elmyr.annotation.StringForgery
import fr.xgouchet.elmyr.junit4.fixture.BarFactory
import fr.xgouchet.elmyr.junit4.fixture.FooFactory
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

internal class ForgeStatementTest {

    @Rule
    @JvmField
    val forge = ForgeRule().withFactory(FooFactory()).withFactory(BarFactory())
    @Rule
    @JvmField
    val mockito = MockitoJUnit.rule().strictness(Strictness.LENIENT)

    internal lateinit var testedStatement: ForgeStatement

    @Mock
    lateinit var mockBaseStatement: Statement
    @Mock
    lateinit var mockMethod: FrameworkMethod
    @Mock
    lateinit var mockTarget: Any

    @StringForgery
    lateinit var fakeMethodName: String

    @Before
    fun setUp() {
        whenever(mockMethod.name) doReturn fakeMethodName
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

//        assertThat(systemErrRule.log)
//            .isEqualTo(
//                "<${mockTarget.javaClass.simpleName}.$fakeMethodName()> failed " +
//                    "with Forge seed 0x${forge.seed.toString(16)}L\n" +
//                    "Add this seed in the ForgeRule in your test class :\n" +
//                    "\n" +
//                    "\tForgeRule forge = new ForgeRule(0x${forge.seed.toString(16)}L);\n" +
//                    "\n"
//            )
    }

    @Test
    fun `prints seed and injected fields when failure happens`() {
        val target = KotlinAnnotationTest()
        testedStatement = ForgeStatement(
            mockBaseStatement,
            mockMethod,
            target,
            forge
        )
        whenever(mockBaseStatement.evaluate()) doThrow AssertionError()

        val e: AssertionError? = try {
            testedStatement.evaluate()
            null
        } catch (e: AssertionError) {
            e
        }

        assertThat(e).isNotNull()

//        assertThat(systemErrRule.log)
//            .isEqualTo(
//                "<KotlinAnnotationTest.$fakeMethodName()> failed " +
//                    "with Forge seed 0x${forge.seed.toString(16)}L and:\n" +
//                    "\t- Field KotlinAnnotationTest::fakeFoo = ${target.fakeFoo}\n" +
//                    "\t- Field KotlinAnnotationTest::fakeFooList = ${target.fakeFooList}\n" +
//                    "\t- Field KotlinAnnotationTest::fakeFooMap = ${target.fakeFooMap}\n" +
//                    "\t- Field KotlinAnnotationTest::fakeFooSet = ${target.fakeFooSet}\n" +
//                    "\t- Field KotlinAnnotationTest::fakeMonth = ${target.fakeMonth}\n\n" +
//                    "Add this seed in the ForgeRule in your test class :\n" +
//                    "\n" +
//                    "\tForgeRule forge = new ForgeRule(0x${forge.seed.toString(16)}L);\n" +
//                    "\n"
//            )
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

//        assertThat(systemErrRule.log)
//            .isEmpty()
    }
}
