package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.inject.CombinedForgeryInjector
import fr.xgouchet.elmyr.inject.ForgeryInjector
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolutionException
import org.junit.jupiter.api.extension.ParameterResolver
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler
import java.lang.reflect.Constructor
import java.util.Locale

/**
 * A JUnit Jupiter extension that can inject forgeries in the test class's fields/properties/method
 * parameters (when annotated with [Forgery]). It can also inject a [Forge] instance in test methods
 * without the need to annotate it.
 *
 * @see Forgery
 * @see Forge
 */
@ForgeConfiguration
class ForgeExtension :
        BeforeAllCallback,
        BeforeTestExecutionCallback,
        TestExecutionExceptionHandler,
        ParameterResolver {

    internal val instanceForge: Forge = Forge()
    private val injector: ForgeryInjector = CombinedForgeryInjector()

    // region Java/Factory

    /**
     * Adds a factory to the forge. This is the best way to extend a forge and provides means to
     * create custom forgeries.
     * @param T the type the [ForgeryFactory] will be able to forge
     * @param forgeryFactory the factory to be used
     * @return the same [ForgeExtension] instance, perfect to chain calls
     */
    inline fun <reified T : Any> withFactory(forgeryFactory: ForgeryFactory<T>): ForgeExtension {
        return withFactory(T::class.java, forgeryFactory)
    }

    /**
     * Adds a factory to the forge. This is the best way to extend a forge and provides means to
     * create custom forgeries.
     * @param T the type the [ForgeryFactory] will be able to forge
     * @param clazz the class of type T
     * @param forgeryFactory the factory to be used
     * @return the same [ForgeExtension] instance, perfect to chain calls
     */
    fun <T : Any> withFactory(clazz: Class<T>, forgeryFactory: ForgeryFactory<T>): ForgeExtension {
        instanceForge.addFactory(clazz, forgeryFactory)
        return this
    }

    // endregion

    // region BeforeAllCallback

    /** @inheritdoc */
    override fun beforeAll(context: ExtensionContext) {
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

    // endregion

    companion object {
        private val NAMESPACE = ExtensionContext.Namespace.create(ForgeExtension::class.java)

        const val SEED_MASK = 0x7FFFFFFFL
    }
}