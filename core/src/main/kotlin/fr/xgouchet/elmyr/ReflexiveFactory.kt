package fr.xgouchet.elmyr

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.valueParameters

internal class ReflexiveFactory(
    private val forge: Forge
) {

    fun <T : Any> getForgery(kClass: KClass<T>): T {
        if (kClass.isData) {
            return getDataClassForgery(kClass)
        } else {
            throw ReflexiveForgeryFactoryException(kClass.java, "only data classes are supported")
        }
    }

    private fun <T : Any> getDataClassForgery(kClass: KClass<T>): T {
        val constructor = kClass.constructors.first()
        val parameters = constructor.valueParameters
        val arguments = Array<Any?>(parameters.size) { null }

        parameters.forEachIndexed { idx, param ->
            arguments[idx] = forgeType(param.type, kClass.java)
        }
        return constructor.call(*arguments)
    }

    private fun forgeType(type: KType, fromClass: Class<*>): Any? {
        val typeClassifier = type.classifier

        return when (typeClassifier) {
            Boolean::class -> forge.aBool()
            Int::class -> forge.anInt()
            Long::class -> forge.aLong()
            Float::class -> forge.aFloat()
            Double::class -> forge.aDouble()
            String::class -> forge.aString()
            java.util.List::class -> forgeList(type.arguments, fromClass)
            java.util.Map::class -> forgeMap(type.arguments, fromClass)
            java.util.Set::class -> forgeList(type.arguments, fromClass).toSet()
            is KClass<*> -> getForgery(typeClassifier)
            else -> throw ReflexiveForgeryFactoryException(fromClass, "Unknown parameter type $type")
        }
    }

    private fun forgeList(arguments: List<KTypeProjection>, fromClass: Class<*>): List<Any?> {
        if (arguments.size != 1) {
            throw ReflexiveForgeryFactoryException(fromClass, "wrong number of type arguments for List")
        }

        val itemType = arguments.first().type
        if (itemType != null) {
            return forge.aList { forgeType(itemType, fromClass) }
        } else {
            throw ReflexiveForgeryFactoryException(fromClass, "unknown type argument for List")
        }
    }

    private fun forgeMap(arguments: List<KTypeProjection>, fromClass: Class<*>): Map<Any?, Any?> {
        if (arguments.size != 2) {
            throw ReflexiveForgeryFactoryException(fromClass, "wrong number of type arguments for Map")
        }
        val keyType = arguments[0].type
        val valueType = arguments[1].type
        if (keyType is KType && valueType is KType) {
            return forge.aMap { forgeType(keyType, fromClass) to forgeType(valueType, fromClass) }
        } else {
            throw ReflexiveForgeryFactoryException(fromClass, "unknown key or value type argument for Map")
        }
    }
}
