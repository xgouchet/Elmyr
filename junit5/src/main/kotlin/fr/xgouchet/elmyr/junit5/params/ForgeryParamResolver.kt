package fr.xgouchet.elmyr.junit5.params

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.Forgery
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.Collection
import java.util.List
import java.util.Set
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import java.lang.reflect.WildcardType

internal class ForgeryParamResolver :
        ForgeryResolver {

    // region ForgeryResolver

    /** @inheritdoc */
    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Boolean {
        return parameterContext.isAnnotated(Forgery::class.java)
    }

    /** @inheritdoc */
    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext,
        forge: Forge
    ): Any? {
        val type = parameterContext.parameter.type
        val parameterizedType = parameterContext.parameter.parameterizedType
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

    // endregion

    // region Internal

    private fun getParameterizedForgery(
        forge: Forge,
        rawType: Type,
        typeArgs: Array<Type>
    ): Any? {
        return when (rawType) {
            in listClasses -> forge.aList { getForgery(forge, typeArgs[0]) }
            in setClasses -> forge.aList { getForgery(forge, typeArgs[0]) }.toSet()
            in mapClasses -> forge.aList {
                val key = getForgery(forge, typeArgs[0])
                val value = getForgery(forge, typeArgs[1])
                key to value
            }.toMap()
            else -> null
        }
    }

    private fun getForgery(forge: Forge, type: Type): Any? {
        return when (type) {
            is Class<*> -> forge.getForgery(type)
            is WildcardType -> getForgery(forge, type.upperBounds.first())
            is ParameterizedType -> getParameterizedForgery(forge, type.rawType, type.actualTypeArguments)
            else -> null
        }
    }

    // endregion

    companion object {
        private val listClasses = arrayOf(List::class.java, Collection::class.java)
        private val setClasses = arrayOf(Set::class.java)
        private val mapClasses = arrayOf(Map::class.java)
    }
}
