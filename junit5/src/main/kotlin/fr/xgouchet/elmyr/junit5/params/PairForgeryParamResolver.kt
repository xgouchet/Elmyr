package fr.xgouchet.elmyr.junit5.params

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.PairForgery
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext

internal object PairForgeryParamResolver : ForgeryResolver {

    // region ForgeryResolver

    /** @inheritdoc */
    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Boolean {
        return parameterContext.isAnnotated(PairForgery::class.java) &&
            Pair::class.java.isAssignableFrom(parameterContext.parameter.type)
    }

    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext,
        forge: Forge
    ): Any? {
        val annotation = parameterContext.findAnnotation(PairForgery::class.java).get()
        val pairType = parameterContext.parameter.parameterizedType

        return resolvePairParameter(annotation, pairType, forge)
    }

    // endregion

    // region Internal

    internal fun resolvePairParameter(
        annotation: PairForgery,
        type: Type,
        forge: Forge
    ): Pair<Any?, Any?> {
        var pairType = type
        while (pairType is WildcardType) {
            pairType = pairType.upperBounds[0]
        }
        check(pairType is ParameterizedType)

        val firstAnnotation = annotation.first
        val firstType = pairType.actualTypeArguments[0]
        val secondAnnotation = annotation.second
        val secondType = pairType.actualTypeArguments[1]

        val forgedFirst = AdvancedForgeryParamResolver.resolveAdvancedParameter(firstAnnotation, firstType, forge)
        val forgedSecond = AdvancedForgeryParamResolver.resolveAdvancedParameter(secondAnnotation, secondType, forge)
        return forgedFirst to forgedSecond
    }

    // endregion
}
