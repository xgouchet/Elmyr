package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeConfigurator
import fr.xgouchet.elmyr.annotation.DoubleForgery
import fr.xgouchet.elmyr.annotation.FloatForgery
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.annotation.LongForgery
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
        val annotated = parameterContext.isAnnotated(Forgery::class.java) ||
                parameterContext.isAnnotated(IntForgery::class.java) ||
                parameterContext.isAnnotated(LongForgery::class.java) ||
                parameterContext.isAnnotated(FloatForgery::class.java) ||
                parameterContext.isAnnotated(DoubleForgery::class.java)
        if (annotated && parameterContext.declaringExecutable is Constructor<*>) {
            throw IllegalStateException(
                    "@Forgery is not supported on constructor parameters. " +
                            "Please use field injection instead."
            )
        }
        val parameterType = parameterContext.parameter.type

        return verifyType(parameterType, parameterContext)
    }

    /** @inheritdoc */
    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Any? {
        return when {
            parameterContext.parameter.type == Forge::class.java -> {
                instanceForge
            }
            parameterContext.isAnnotated(Forgery::class.java) -> {
                resolveForgeryParameter(parameterContext)
            }
            parameterContext.isAnnotated(IntForgery::class.java) -> {
                resolveIntForgeryParameter(parameterContext.findAnnotation(IntForgery::class.java).get())
            }
            parameterContext.isAnnotated(LongForgery::class.java) -> {
                resolveLongForgeryParameter(parameterContext.findAnnotation(LongForgery::class.java).get())
            }
            parameterContext.isAnnotated(FloatForgery::class.java) -> {
                resolveFloatForgeryParameter(parameterContext.findAnnotation(FloatForgery::class.java).get())
            }
            parameterContext.isAnnotated(DoubleForgery::class.java) -> {
                resolveDoubleForgeryParameter(parameterContext.findAnnotation(DoubleForgery::class.java).get())
            }
            else -> {
                null
            }
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

        while (currentContext != context.root) {
            val annotation = AnnotationSupport
                    .findAnnotation<ForgeConfiguration>(
                            currentContext.element,
                            ForgeConfiguration::class.java
                    )

            if (annotation.isPresent) {
                val configurator = annotation.get().value.java.newInstance()
                result.add(configurator)
            }

            currentContext = currentContext.parent.get()
        }

        return result
    }

    private fun verifyType(parameterType: Class<*>, parameterContext: ParameterContext): Boolean {
        if (parameterContext.isAnnotated(IntForgery::class.java)) {
            check(parameterType == Int::class.java) {
                "@IntForgery can only be used on a Java int or Integer, or a Kotlin Int"
            }
            return true
        }

        if (parameterContext.isAnnotated(LongForgery::class.java)) {
            check(parameterType == Long::class.java) {
                "@LongForgery can only be used on a Java long or Long, or a Kotlin Long"
            }
            return true
        }

        if (parameterContext.isAnnotated(FloatForgery::class.java)) {
            check(parameterType == Float::class.java) {
                "@FloatForgery can only be used on a Java float or Float, or a Kotlin Float"
            }
            return true
        }

        if (parameterContext.isAnnotated(DoubleForgery::class.java)) {
            check(parameterType == Double::class.java) {
                "@DoubleForgery can only be used on a Java double or Double, or a Kotlin Double"
            }
            return true
        }

        if (parameterType == Forge::class.java) return true

        return parameterContext.isAnnotated(Forgery::class.java)
    }

    private fun resolveForgeryParameter(parameterContext: ParameterContext): Any? {
        val type = parameterContext.parameter.type
        val parameterizedType = parameterContext.parameter.parameterizedType
        val forge = instanceForge
        return if (parameterizedType is ParameterizedType) {
            getParameterizedForgery(
                    forge,
                    parameterizedType.rawType,
                    parameterizedType.actualTypeArguments
            ) ?: forge.getForgery(type)
        } else {
            forge.getForgery(type)
        }
    }

    private fun resolveIntForgeryParameter(annotation: IntForgery): Int {
        return if (annotation.standardDeviation >= 0) {
            check(annotation.min == Int.MIN_VALUE) {
                "You can only use an IntForgery with min and max or with mean and standardDeviation"
            }
            check(annotation.max == Int.MAX_VALUE) {
                "You can only use an IntForgery with min and max or with mean and standardDeviation"
            }
            instanceForge.aGaussianInt(annotation.mean, annotation.standardDeviation)
        } else {
            check(annotation.mean == 0) {
                "You can only use an IntForgery with min and max or with mean and standardDeviation"
            }
            instanceForge.anInt(annotation.min, annotation.max)
        }
    }

    private fun resolveLongForgeryParameter(annotation: LongForgery): Long {
        return if (annotation.standardDeviation >= 0) {
            check(annotation.min == Long.MIN_VALUE) {
                "You can only use an LongForgery with min and max or with mean and standardDeviation"
            }
            check(annotation.max == Long.MAX_VALUE) {
                "You can only use an LongForgery with min and max or with mean and standardDeviation"
            }
            instanceForge.aGaussianLong(annotation.mean, annotation.standardDeviation)
        } else {
            check(annotation.mean == 0L) {
                "You can only use an LongForgery with min and max or with mean and standardDeviation"
            }
            instanceForge.aLong(annotation.min, annotation.max)
        }
    }

    private fun resolveFloatForgeryParameter(annotation: FloatForgery): Float {
        return if (!annotation.standardDeviation.isNaN()) {
            check(annotation.min == -Float.MAX_VALUE) {
                "You can only use an FloatForgery with min and max or with mean and standardDeviation"
            }
            check(annotation.max == Float.MAX_VALUE) {
                "You can only use an FloatForgery with min and max or with mean and standardDeviation"
            }
            instanceForge.aGaussianFloat(annotation.mean, annotation.standardDeviation)
        } else {
            check(annotation.mean == 0f) {
                "You can only use an FloatForgery with min and max or with mean and standardDeviation"
            }
            instanceForge.aFloat(annotation.min, annotation.max)
        }
    }

    private fun resolveDoubleForgeryParameter(annotation: DoubleForgery): Double {
        return if (!annotation.standardDeviation.isNaN()) {
            check(annotation.min == -Double.MAX_VALUE) {
                "You can only use an DoubleForgery with min and max or with mean and standardDeviation"
            }
            check(annotation.max == Double.MAX_VALUE) {
                "You can only use an DoubleForgery with min and max or with mean and standardDeviation"
            }
            instanceForge.aGaussianDouble(annotation.mean, annotation.standardDeviation)
        } else {
            check(annotation.mean == 0.0) {
                "You can only use an DoubleForgery with min and max or with mean and standardDeviation"
            }
            instanceForge.aDouble(annotation.min, annotation.max)
        }
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
