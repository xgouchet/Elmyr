package fr.xgouchet.elmyr.junit5.params

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.AdvancedForgery
import java.lang.reflect.Type
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext

internal object AdvancedForgeryParamResolver : ForgeryResolver {

    // region ForgeryResolver

    /** @inheritdoc */
    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Boolean {
        return parameterContext.isAnnotated(AdvancedForgery::class.java)
    }

    /** @inheritdoc */
    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext,
        forge: Forge
    ): Any? {
        val annotation = parameterContext.findAnnotation(AdvancedForgery::class.java).get()
        val type = parameterContext.parameter.parameterizedType
        return resolveAdvancedParameter(annotation, type, forge)
    }

    // endregion

    // region Internal

    internal fun resolveAdvancedParameter(
        annotation: AdvancedForgery,
        type: Type,
        forge: Forge
    ): Any? {
        return when {
            annotation.string.isNotEmpty() -> resolveStringParameter(annotation, type, forge)
            annotation.int.isNotEmpty() -> resolveIntParameter(annotation, type, forge)
            annotation.long.isNotEmpty() -> resolveLongParameter(annotation, type, forge)
            annotation.float.isNotEmpty() -> resolveFloatParameter(annotation, type, forge)
            annotation.double.isNotEmpty() -> resolveDoubleParameter(annotation, type, forge)
            annotation.map.isNotEmpty() -> resolveMapParameter(annotation, type, forge)
            annotation.pair.isNotEmpty() -> resolvePairParameter(annotation, type, forge)
            else -> ForgeryParamResolver.resolveParameter(type, forge)
        }
    }

    private fun resolveDoubleParameter(
        annotation: AdvancedForgery,
        type: Type,
        forge: Forge
    ): Any? {
        val usingAnnotation = forge.anElementFrom(*annotation.double)
        return DoubleForgeryParamResolver.resolveParameter(
            usingAnnotation,
            type,
            forge
        )
    }

    private fun resolveFloatParameter(
        annotation: AdvancedForgery,
        type: Type,
        forge: Forge
    ): Any? {
        val usingAnnotation = forge.anElementFrom(*annotation.float)
        return FloatForgeryParamResolver.resolveParameter(
            usingAnnotation,
            type,
            forge
        )
    }

    private fun resolveLongParameter(
        annotation: AdvancedForgery,
        type: Type,
        forge: Forge
    ): Any? {
        val usingAnnotation = forge.anElementFrom(*annotation.long)
        return LongForgeryParamResolver.resolveParameter(
            usingAnnotation,
            type,
            forge
        )
    }

    private fun resolveIntParameter(
        annotation: AdvancedForgery,
        type: Type,
        forge: Forge
    ): Any? {
        val usingAnnotation = forge.anElementFrom(*annotation.int)
        return IntForgeryParamResolver.resolveParameter(
            usingAnnotation,
            type,
            forge
        )
    }

    private fun resolveStringParameter(
        annotation: AdvancedForgery,
        type: Type,
        forge: Forge
    ): Any? {
        val usingAnnotation = forge.anElementFrom(*annotation.string)
        return StringForgeryParamResolver.resolveParameter(
            usingAnnotation,
            type,
            forge
        )
    }

    private fun resolveMapParameter(
        annotation: AdvancedForgery,
        type: Type,
        forge: Forge
    ): Map<Any?, Any?> {
        val usingAnnotation = forge.anElementFrom(*annotation.map)
        return MapForgeryParamResolver.resolveMapParameter(
            usingAnnotation,
            type,
            forge
        )
    }

    private fun resolvePairParameter(
        annotation: AdvancedForgery,
        type: Type,
        forge: Forge
    ): Pair<Any?, Any?> {
        val usingAnnotation = forge.anElementFrom(*annotation.pair)
        return PairForgeryParamResolver.resolvePairParameter(
            usingAnnotation,
            type,
            forge
        )
    }

    // endregion
}
