package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeConfigurator
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.inject.DefaultForgeryInjector
import fr.xgouchet.elmyr.inject.ForgeryInjector
import fr.xgouchet.elmyr.junit5.params.AdvancedForgeryParamResolver
import fr.xgouchet.elmyr.junit5.params.BooleanForgeryParamResolver
import fr.xgouchet.elmyr.junit5.params.DoubleForgeryParamResolver
import fr.xgouchet.elmyr.junit5.params.FloatForgeryParamResolver
import fr.xgouchet.elmyr.junit5.params.ForgeParamResolver
import fr.xgouchet.elmyr.junit5.params.ForgeryParamResolver
import fr.xgouchet.elmyr.junit5.params.IntForgeryParamResolver
import fr.xgouchet.elmyr.junit5.params.LongForgeryParamResolver
import fr.xgouchet.elmyr.junit5.params.MapForgeryParamResolver
import fr.xgouchet.elmyr.junit5.params.RegexForgeryParamResolver
import fr.xgouchet.elmyr.junit5.params.StringForgeryParamResolver
import java.lang.reflect.Constructor
import java.util.Locale
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
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
    BeforeEachCallback,
    BeforeTestExecutionCallback,
    TestExecutionExceptionHandler,
    ParameterResolver {

    internal val instanceForge: Forge = Forge()
    private val injector: ForgeryInjector = DefaultForgeryInjector()

    private val parameterResolvers = listOf(
        ForgeParamResolver,
        BooleanForgeryParamResolver,
        IntForgeryParamResolver,
        LongForgeryParamResolver,
        FloatForgeryParamResolver,
        DoubleForgeryParamResolver,
        StringForgeryParamResolver,
        RegexForgeryParamResolver,
        ForgeryParamResolver,
        AdvancedForgeryParamResolver,
        MapForgeryParamResolver
    )

    // region BeforeAllCallback

    /** @inheritdoc */
    override fun beforeAll(context: ExtensionContext) {
        val configurators = getConfigurators(context)

        configurators.forEach {
            it.configure(instanceForge)
        }
    }

    // endregion

    // region BeforeEachCallback

    /** @inheritdoc */
    override fun beforeEach(context: ExtensionContext) {
        resetSeed(context)
        val target = context.requiredTestInstance
        injector.inject(instanceForge, target)
    }

    // endregion

    // region BeforeTestExecutionCallback

    /** @inheritdoc */
    override fun beforeTestExecution(context: ExtensionContext) {
    }

    // endregion

    // region TestExecutionExceptionHandler

    /** @inheritdoc */
    override fun handleTestExecutionException(context: ExtensionContext, throwable: Throwable) {
        val configuration = getConfigurations(context).firstOrNull()
        val message = if (configuration == null) {
            "<%s.%s()> failed with Forge seed 0x%xL\n" +
                "Add the following @ForgeConfiguration annotation to your test class :\n\n" +
                "\t@ForgeConfiguration(seed = 0x%xL)\n"
        } else {
            "<%s.%s()> failed with Forge seed 0x%xL\n" +
                "Add the seed in your @ForgeConfiguration annotation :\n\n" +
                "\t@ForgeConfiguration(value = ${configuration.value.simpleName}::class, seed = 0x%xL)\n"
        }
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
        val isSupported = parameterResolvers.any {
            it.supportsParameter(parameterContext, extensionContext)
        }

        if (isSupported && parameterContext.declaringExecutable is Constructor<*>) {
            throw IllegalStateException(
                "@Forgery is not supported on constructor parameters. " +
                    "Please use field injection instead."
            )
        }

        return isSupported
    }

    /** @inheritdoc */
    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Any? {
        val resolver = parameterResolvers.firstOrNull {
            it.supportsParameter(parameterContext, extensionContext)
        }
        return resolver?.resolveParameter(parameterContext, extensionContext, instanceForge)
    }

    // endregion

    // region Internal

    private fun resetSeed(context: ExtensionContext) {
        val configurations = getConfigurations(context)
        val seed = configurations.map { it.seed }
            .firstOrNull { it != 0L }
        instanceForge.seed = seed ?: Forge.seed()
    }

    private fun getConfigurations(context: ExtensionContext): List<ForgeConfiguration> {
        val result = mutableListOf<ForgeConfiguration>()
        var currentContext = context

        while (currentContext != context.root) {
            val annotation = AnnotationSupport
                .findAnnotation<ForgeConfiguration>(
                    currentContext.element,
                    ForgeConfiguration::class.java
                )

            if (annotation.isPresent) {
                result.add(annotation.get())
            }

            if (currentContext.parent.isPresent) {
                currentContext = currentContext.parent.get()
            } else {
                break
            }
        }

        return result
    }

    private fun getConfigurators(context: ExtensionContext): List<ForgeConfigurator> {
        return getConfigurations(context)
            .map { it.value.java.newInstance() }
    }

    // endregion
}
