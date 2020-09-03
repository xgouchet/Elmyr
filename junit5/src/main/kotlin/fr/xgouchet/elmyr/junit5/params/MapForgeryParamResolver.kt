package fr.xgouchet.elmyr.junit5.params

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.MapForgery
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext

internal object MapForgeryParamResolver : ForgeryResolver {

    // region ForgeryResolver

    /** @inheritdoc */
    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Boolean {
        return parameterContext.isAnnotated(MapForgery::class.java) &&
            java.util.Map::class.java.isAssignableFrom(parameterContext.parameter.type)
    }

    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext,
        forge: Forge
    ): Any? {
        val annotation = parameterContext.findAnnotation(MapForgery::class.java).get()
        val mapType = parameterContext.parameter.parameterizedType

        return resolveMapParameter(annotation, mapType, forge)
    }

    // endregion

    // region Internal

    internal fun resolveMapParameter(
        annotation: MapForgery,
        type: Type,
        forge: Forge
    ): Map<Any?, Any?> {
        var mapType = type
        while (mapType is WildcardType) {
            mapType = mapType.upperBounds[0]
        }
        check(mapType is ParameterizedType)

        val keyAnnotation = annotation.key
        val keyType = mapType.actualTypeArguments[0]
        val valueAnnotation = annotation.value
        val valueType = mapType.actualTypeArguments[1]

        return forge.aMap {
            val forgedKey = AdvancedForgeryParamResolver.resolveAdvancedParameter(keyAnnotation, keyType, forge)
            val forgedValue = AdvancedForgeryParamResolver.resolveAdvancedParameter(valueAnnotation, valueType, forge)
            forgedKey to forgedValue
        }
    }

    // endregion
}
