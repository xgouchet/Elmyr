package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeConfigurator
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.inject.DefaultForgeryInjector
import fr.xgouchet.elmyr.inject.ForgeryInjector
import java.lang.reflect.Constructor
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.Collection as JavaCollection
import java.util.List as JavaList
import java.util.Locale
import java.util.Set as JavaSet
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
        val message = "<%s.%s()> failed with Forge seed 0x%xL\n" +
                "Add the following line in your @BeforeEach method to reproduce :\n\n" +
                "\tforge.setSeed(0x%xL);\n"
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
        val type = parameterContext.parameter.type
        val parameterizedType = parameterContext.parameter.parameterizedType
        val forge = instanceForge
        return if (type == Forge::class.java) {
            forge
        } else if (parameterizedType is ParameterizedType) {
            getParameterizedForgery(
                    forge,
                    parameterizedType.rawType,
                    parameterizedType.actualTypeArguments
            ) ?: forge.getForgery(type)
        } else {
            forge.getForgery(type)
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

    private fun getParameterizedForgery(
        forge: Forge,
        rawType: Type,
        typeArgs: Array<Type>
    ): Any? {
        val firstTypeArg = typeArgs[0] as? Class<*> ?: return null
        if (rawType in listClasses) {
            return forge.aList { getForgery(firstTypeArg) }
        } else if (rawType in setClasses) {
            return forge.aList { getForgery(firstTypeArg) }.toSet()
        } else {
            return null
        }
    }

    // endregion

    companion object {
        private val listClasses = arrayOf(JavaList::class.java, JavaCollection::class.java)
        private val setClasses = arrayOf(JavaSet::class.java)

        private val NAMESPACE = ExtensionContext.Namespace.create(ForgeExtension::class.java)

        const val SEED_MASK = 0x7FFFFFFFL
    }
}
