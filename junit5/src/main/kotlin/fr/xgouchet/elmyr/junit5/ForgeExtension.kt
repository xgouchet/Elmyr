package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeConfigurator
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.inject.DefaultForgeryInjector
import fr.xgouchet.elmyr.inject.ForgeryInjector
import java.lang.reflect.Constructor
import java.util.Locale
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolutionException
import org.junit.jupiter.api.extension.ParameterResolver
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler
import org.junit.platform.commons.support.AnnotationSupport

/**
 * A JUnit Jupiter extension that can inject forgeries in the test class's fields/properties/method
 * parameters (when annotated with [Forgery]). It can also inject a [Forge] instance in test methods
 * without the need to annotate it.
 *
 * @see Forgery
 * @see Forge
 */
class ForgeExtension :
        BeforeAllCallback,
        BeforeTestExecutionCallback,
        TestExecutionExceptionHandler,
        ParameterResolver {

    internal val instanceForge: Forge = Forge()
    private val injector: ForgeryInjector = DefaultForgeryInjector()

    // region BeforeAllCallback

    /** @inheritdoc */
    override fun beforeAll(context: ExtensionContext) {
        val configurators = getConfigurators(context)

        configurators.forEach {
            it.configure(instanceForge)
        }
    }

    // endregion

    // region BeforeTestExecutionCallback

    /** @inheritdoc */
    override fun beforeTestExecution(context: ExtensionContext) {
        resetSeed(context)
        val target = context.requiredTestInstance
        injector.inject(instanceForge, target)
    }

    // endregion

    // region TestExecutionExceptionHandler

    /** @inheritdoc */
    override fun handleTestExecutionException(context: ExtensionContext, throwable: Throwable) {
        val message = "<%s.%s()> failed with Forge seed 0x%x\n" +
                "Add the following line in your @Before method to reproduce :\n\n" +
                "\tforger.resetSeed(0x%xL)\n"
        System.err.println(
                message.format(
                        Locale.US,
                        context.requiredTestInstance.javaClass.simpleName,
                        context.requiredTestMethod.name,
                        instanceForge.seed,
                        instanceForge.seed
                )
        )

        throw throwable
    }

    // endregion

    // region ParameterResolver

    /** @inheritdoc */
    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Boolean {
        val annotated = parameterContext.isAnnotated(Forgery::class.java)
        if (annotated && parameterContext.declaringExecutable is Constructor<*>) {
            throw ParameterResolutionException(
                    "@Forgery is not supported on constructor parameters. " +
                            "Please use field injection instead."
            )
        }
        val parameterType = parameterContext.parameter.type
        return annotated || (parameterType == Forge::class.java)
    }

    /** @inheritdoc */
    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Any? {
        val parameterType = parameterContext.parameter.type
        val forge = instanceForge
        return if (parameterType == Forge::class.java) {
            forge
        } else {
            forge.getForgery(parameterType)
        }
    }

    // endregion

    // region Internal

    private fun resetSeed(extensionContext: ExtensionContext) {
        val seed = (System.nanoTime() and SEED_MASK)
        instanceForge.seed = seed
    }

    private fun getConfigurators(context: ExtensionContext): List<ForgeConfigurator> {
        val result = mutableListOf<ForgeConfigurator>()
        var currentContext = context

        do {
            val annotation = AnnotationSupport
                    .findAnnotation<ForgeConfiguration>(
                            currentContext.element,
                            ForgeConfiguration::class.java
                    )

            if (annotation.isPresent) {
                val configurator = annotation.get().value.java.newInstance()
                result.add(configurator)
            }

            if (!currentContext.parent.isPresent) {
                break
            }

            currentContext = currentContext.parent.get()
        } while (currentContext !== context.root)

        return result
    }

    // endregion

    companion object {
        private val NAMESPACE = ExtensionContext.Namespace.create(ForgeExtension::class.java)

        const val SEED_MASK = 0x7FFFFFFFL
    }
}
