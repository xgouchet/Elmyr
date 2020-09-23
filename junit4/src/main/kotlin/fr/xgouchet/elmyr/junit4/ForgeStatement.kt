package fr.xgouchet.elmyr.junit4

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.inject.DefaultForgeryInjector
import fr.xgouchet.elmyr.inject.ForgeryInjector
import java.lang.reflect.Type
import java.util.Locale
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.MultipleFailureException
import org.junit.runners.model.Statement

internal class ForgeStatement(
    private val base: Statement,
    private val method: FrameworkMethod,
    private val target: Any,
    private val forge: Forge
) : Statement(), ForgeryInjector.Listener {

    private val injector: ForgeryInjector = DefaultForgeryInjector()
    private val injectedData: MutableList<Triple<String, String, Any?>> = mutableListOf()

    // region Statement

    /** @inheritdoc */
    @Suppress("TooGenericExceptionCaught")
    override fun evaluate() {
        val errors = mutableListOf<Throwable>()

        performQuietly(errors) { starting() }

        try {
            injector.inject(forge, target, this)
            base.evaluate()
            performQuietly(errors) { succeeded() }
        } catch (e: org.junit.internal.AssumptionViolatedException) {
            errors.add(e)
            performQuietly(errors) { skipped() }
        } catch (e: Throwable) {
            errors.add(e)
            performQuietly(errors) { failed() }
        } finally {
            performQuietly(errors) { finished() }
        }

        MultipleFailureException.assertEmpty(errors)
    }

    // endregion

    // region ForgeryInjector.Listener

    /** @inheritdoc */
    override fun onFieldInjected(declaringClass: Class<*>, fieldType: Type, fieldName: String, value: Any?) {
        injectedData.add(
            Triple(
                declaringClass.simpleName,
                fieldName,
                value
            )
        )
    }

    // endregion

    // region Internal

    @Suppress("TooGenericExceptionCaught")
    private fun performQuietly(
        errors: MutableList<Throwable>,
        operation: () -> Unit
    ) {
        try {
            operation()
        } catch (e: Throwable) {
            errors.add(e)
        }
    }

    @Suppress("EmptyFunctionBlock")
    private fun starting() {
    }

    @Suppress("EmptyFunctionBlock")
    private fun succeeded() {
    }

    @Suppress("EmptyFunctionBlock")
    private fun skipped() {
    }

    private fun failed() {
        val errorMessage = "<%s.%s()> failed with Forge seed 0x%xL".format(
            Locale.US,
            target.javaClass.simpleName,
            method.name,
            forge.seed
        )

        val injectedMessage = if (injectedData.isEmpty()) "" else {
            injectedData.joinToString(
                separator = "\n",
                prefix = " and:\n",
                postfix = "\n"
            ) { "\t- Field ${it.first}::${it.second} = ${it.third}" }
        }

        val helpMessage = "\nAdd this seed in the ForgeRule in your test class :\n\n" +
                "\tForgeRule forge = new ForgeRule(0x%xL);\n".format(
                    Locale.US,
                    forge.seed
                )

        System.err.println(
            errorMessage + injectedMessage + helpMessage
        )
    }

    @Suppress("EmptyFunctionBlock")
    private fun finished() {
    }

    // endregion
}
