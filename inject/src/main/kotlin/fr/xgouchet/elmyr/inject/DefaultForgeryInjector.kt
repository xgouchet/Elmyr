package fr.xgouchet.elmyr.inject

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.AdvancedForgery
import fr.xgouchet.elmyr.annotation.BoolForgery
import fr.xgouchet.elmyr.annotation.DoubleForgery
import fr.xgouchet.elmyr.annotation.FloatForgery
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.annotation.LongForgery
import fr.xgouchet.elmyr.annotation.MapForgery
import fr.xgouchet.elmyr.annotation.RegexForgery
import fr.xgouchet.elmyr.annotation.StringForgery
import fr.xgouchet.elmyr.annotation.StringForgeryType
import fr.xgouchet.elmyr.inject.reflect.invokePrivate
import java.lang.IllegalStateException
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.superclasses

/**
 * The default implementation of a [ForgeryInjector].
 *
 * It can inject forgeries both in Java fields and Kotlin properties.
 */
@Suppress("LiftReturnOrAssignment")
class DefaultForgeryInjector : ForgeryInjector {

    // region ForgeryInjector

    override fun inject(forge: Forge, target: Any) {
        val classesToProcess = mutableSetOf<KClass<*>>()
        classesToProcess.add(target.javaClass.kotlin)

        while (classesToProcess.isNotEmpty()) {
            val classToProcess = classesToProcess.first()
            if (classToProcess != Any::class) {
                injectInClass(forge, classToProcess, target)
                classToProcess.superclasses.forEach {
                    classesToProcess.add(it)
                }
            }
            classesToProcess.remove(classToProcess)
        }
    }

    // endregion

    // region Internal

    @Suppress("ThrowableNotThrown")
    private fun injectInClass(
        forge: Forge,
        clazz: KClass<*>,
        target: Any
    ) {
        val invalidProperties = clazz.declaredMembers
            .filterIsInstance<KProperty<*>>()
            .filter { it !is KMutableProperty }
            .filter {
                it.annotations.any { annotation ->
                    annotation is Forgery ||
                        annotation is BoolForgery ||
                        annotation is IntForgery ||
                        annotation is LongForgery ||
                        annotation is FloatForgery ||
                        annotation is DoubleForgery ||
                        annotation is StringForgery ||
                        annotation is RegexForgery ||
                        annotation is AdvancedForgery ||
                        annotation is MapForgery
                }
            }
        when (invalidProperties.size) {
            0 -> injectInClassSafe(clazz, forge, target)
            1 -> throw ForgeryInjectorException.withProperty(target, invalidProperties.first())
            else -> throw ForgeryInjectorException.withProperties(target, invalidProperties)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun injectInClassSafe(clazz: KClass<*>, forge: Forge, target: Any) {
        val mutableProperties = clazz.declaredMembers.filterIsInstance<KMutableProperty<*>>()

        val errors = mutableListOf<Throwable>()

        for (property in mutableProperties) {
            try {
                injectInProperty(forge, property, target)
            } catch (e: Throwable) {
                errors.add(e)
            }
        }

        if (errors.isNotEmpty()) {
            throw ForgeryInjectorException.withErrors(target, errors)
        }
    }

    private fun injectInProperty(
        forge: Forge,
        property: KMutableProperty<*>,
        target: Any
    ) {
        var value: Any? = null
        for (annotation in property.annotations) {
            value = when (annotation) {
                is Forgery -> getForgery(forge, property)
                is BoolForgery -> getBooleanForgery(forge, annotation, property)
                is IntForgery -> getIntForgery(forge, annotation, property)
                is LongForgery -> getLongForgery(forge, annotation, property)
                is FloatForgery -> getFloatForgery(forge, annotation, property)
                is DoubleForgery -> getDoubleForgery(forge, annotation, property)
                is StringForgery -> getStringForgery(forge, annotation, property)
                is RegexForgery -> getRegexForgery(forge, annotation, property)
                is AdvancedForgery -> getAdvancedForgery(forge, annotation, property)
                is MapForgery -> getMapForgery(forge, annotation, property)
                else -> null
            }
            if (value != null) break
        }

        if (value != null) {
            property.setter.invokePrivate(target, value)
        }
    }

    private fun getForgery(
        forge: Forge,
        property: KMutableProperty<*>
    ): Any? {
        return getGenericForgery(property.returnType, forge)
            ?: uninjectable(property)
    }

    private fun getBooleanForgery(
        forge: Forge,
        annotation: BoolForgery,
        property: KMutableProperty<*>
    ): Any {
        check(annotation.probability >= 0f) {
            "You can only use an BoolForgery with a probability between 0f and 1f"
        }
        check(annotation.probability <= 1f) {
            "You can only use an BoolForgery with a probability between 0f and 1f"
        }
        val forgery: Forge.() -> Boolean = { aBool(annotation.probability) }
        val result = getPrimitiveForgery(property.returnType, Boolean::class, forge, forgery)
        return result ?: uninjectable(property)
    }

    private fun getIntForgery(
        forge: Forge,
        annotation: IntForgery,
        property: KMutableProperty<*>
    ): Any {
        val forgery: Forge.() -> Int
        if (annotation.standardDeviation >= 0) {
            check(annotation.min == Int.MIN_VALUE) {
                "You can only use an IntForgery with min and max or with mean and standardDeviation"
            }
            check(annotation.max == Int.MAX_VALUE) {
                "You can only use an IntForgery with min and max or with mean and standardDeviation"
            }
            forgery = { aGaussianInt(annotation.mean, annotation.standardDeviation) }
        } else {
            check(annotation.mean == 0) {
                "You can only use an IntForgery with min and max or with mean and standardDeviation"
            }
            forgery = { anInt(annotation.min, annotation.max) }
        }
        val result = getPrimitiveForgery(property.returnType, Int::class, forge, forgery)
        return result ?: uninjectable(property)
    }

    private fun getLongForgery(
        forge: Forge,
        annotation: LongForgery,
        property: KMutableProperty<*>
    ): Any {
        val forgery: Forge.() -> Long
        if (annotation.standardDeviation >= 0) {
            check(annotation.min == Long.MIN_VALUE) {
                "You can only use an LongForgery with min and max or with mean and standardDeviation"
            }
            check(annotation.max == Long.MAX_VALUE) {
                "You can only use an LongForgery with min and max or with mean and standardDeviation"
            }
            forgery = { aGaussianLong(annotation.mean, annotation.standardDeviation) }
        } else {
            check(annotation.mean == 0L) {
                "You can only use an LongForgery with min and max or with mean and standardDeviation"
            }
            forgery = { aLong(annotation.min, annotation.max) }
        }
        val result = getPrimitiveForgery(property.returnType, Long::class, forge, forgery)
        return result ?: uninjectable(property)
    }

    private fun getFloatForgery(
        forge: Forge,
        annotation: FloatForgery,
        property: KMutableProperty<*>
    ): Any {
        val forgery: Forge.() -> Float
        if (!annotation.standardDeviation.isNaN()) {
            check(annotation.min == -Float.MAX_VALUE) {
                "You can only use an FloatForgery with min and max or with mean and standardDeviation"
            }
            check(annotation.max == Float.MAX_VALUE) {
                "You can only use an FloatForgery with min and max or with mean and standardDeviation"
            }
            forgery = { aGaussianFloat(annotation.mean, annotation.standardDeviation) }
        } else {
            check(annotation.mean == 0f) {
                "You can only use an FloatForgery with min and max or with mean and standardDeviation"
            }
            forgery = { aFloat(annotation.min, annotation.max) }
        }

        val result = getPrimitiveForgery(property.returnType, Float::class, forge, forgery)
        return result ?: uninjectable(property)
    }

    private fun getDoubleForgery(
        forge: Forge,
        annotation: DoubleForgery,
        property: KMutableProperty<*>
    ): Any {
        val forgery: Forge.() -> Double
        if (!annotation.standardDeviation.isNaN()) {
            check(annotation.min == -Double.MAX_VALUE) {
                "You can only use an DoubleForgery with min and max or with mean and standardDeviation"
            }
            check(annotation.max == Double.MAX_VALUE) {
                "You can only use an DoubleForgery with min and max or with mean and standardDeviation"
            }
            forgery = { aGaussianDouble(annotation.mean, annotation.standardDeviation) }
        } else {
            check(annotation.mean == 0.0) {
                "You can only use an DoubleForgery with min and max or with mean and standardDeviation"
            }
            forgery = { aDouble(annotation.min, annotation.max) }
        }

        val result = getPrimitiveForgery(property.returnType, Double::class, forge, forgery)
        return result ?: uninjectable(property)
    }

    private fun getStringForgery(
        forge: Forge,
        annotation: StringForgery,
        property: KMutableProperty<*>
    ): Any {
        val forgery: Forge.() -> String = when (annotation.value) {
            StringForgeryType.ALPHABETICAL -> { -> anAlphabeticalString(annotation.case) }
            StringForgeryType.ALPHA_NUMERICAL -> { -> anAlphaNumericalString(annotation.case) }
            StringForgeryType.NUMERICAL -> { -> aNumericalString() }
            StringForgeryType.HEXADECIMAL -> { -> anHexadecimalString(annotation.case) }
            StringForgeryType.WHITESPACE -> { -> aWhitespaceString() }
            StringForgeryType.ASCII -> { -> anAsciiString() }
            StringForgeryType.ASCII_EXTENDED -> { -> anExtendedAsciiString() }
        }
        val result = getPrimitiveForgery(property.returnType, String::class, forge, forgery)
        return result ?: uninjectable(property)
    }

    private fun getRegexForgery(
        forge: Forge,
        annotation: RegexForgery,
        property: KMutableProperty<*>
    ): Any {
        val forgery: Forge.() -> String = { aStringMatching(annotation.value) }
        val result = getPrimitiveForgery(property.returnType, String::class, forge, forgery)
        return result ?: uninjectable(property)
    }

    private fun getAdvancedForgery(
        forge: Forge,
        annotation: AdvancedForgery,
        property: KMutableProperty<*>
    ): Any? {
        return if (annotation.string.isNotEmpty()) {
            val usingAnnotation = forge.anElementFrom(*annotation.string)
            getStringForgery(forge, usingAnnotation, property)
        } else if (annotation.int.isNotEmpty()) {
            val usingAnnotation = forge.anElementFrom(*annotation.int)
            getIntForgery(forge, usingAnnotation, property)
        } else if (annotation.long.isNotEmpty()) {
            val usingAnnotation = forge.anElementFrom(*annotation.long)
            getLongForgery(forge, usingAnnotation, property)
        } else if (annotation.float.isNotEmpty()) {
            val usingAnnotation = forge.anElementFrom(*annotation.float)
            getFloatForgery(forge, usingAnnotation, property)
        } else if (annotation.double.isNotEmpty()) {
            val usingAnnotation = forge.anElementFrom(*annotation.double)
            getDoubleForgery(forge, usingAnnotation, property)
        } else if (annotation.map.isNotEmpty()) {
            val usingAnnotation = forge.anElementFrom(*annotation.map)
            getMapForgery(forge, usingAnnotation, property)
        } else {
            getForgery(forge, property)
        }
    }

    private fun getMapForgery(
        forge: Forge,
        annotation: MapForgery,
        property: KMutableProperty<*>
    ): Map<Any?, Any?> {
        val mapType = property.returnType
        check(mapType.classifier in knownMaps)

        val keyAnnotation = annotation.key
        val keyType = mapType.arguments[0].type ?: uninjectable(property)
        val keyProperty = KMapProperty<Any>(keyType)
        val valueAnnotation = annotation.value
        val valueType = mapType.arguments[1].type ?: uninjectable(property)
        val valueProperty = KMapProperty<Any>(valueType)

        return forge.aMap {
            val forgedKey = getAdvancedForgery(forge, keyAnnotation, keyProperty)
            val forgedValue = getAdvancedForgery(forge, valueAnnotation, valueProperty)
            forgedKey to forgedValue
        }
    }

    private fun getGenericForgery(type: KType, forge: Forge): Any? {
        val arguments = type.arguments
        val classifier = type.classifier
        if (classifier !is KClass<*>) return null

        return if (arguments.isEmpty()) {
            forge.getForgery(classifier.java)
        } else {
            getParameterizedForgery(forge, arguments, classifier)
        }
    }

    @Suppress("UnsafeCallOnNullableType")
    private fun getParameterizedForgery(
        forge: Forge,
        arguments: List<KTypeProjection>,
        classifier: KClass<*>
    ): Any? {
        return when (classifier) {
            in knownLists -> forge.aList { getGenericForgery(arguments[0].type!!, forge) }
            in knownSets -> forge.aList { getGenericForgery(arguments[0].type!!, forge) }.toSet()
            in knownMaps -> forge.aMap {
                val key = getGenericForgery(arguments[0].type!!, forge)
                val value = getGenericForgery(arguments[1].type!!, forge)
                key to value
            }
            else -> {
                null
            }
        }
    }

    private fun getPrimitiveForgery(
        type: KType,
        klass: KClass<*>,
        forge: Forge,
        forgery: Forge.() -> Any?
    ): Any? {
        val arguments = type.arguments
        val classifier = type.classifier
        if (classifier !is KClass<*>) return null

        return if (arguments.isEmpty()) {
            if (classifier == klass || classifier.java == klass.java) {
                forge.forgery()
            } else {
                throw IllegalStateException("Unable to forge primitive $klass on proprety with type $type")
            }
        } else {
            getParameterizedPrimitiveForgery(arguments, classifier, klass, forge, forgery)
        }
    }

    @Suppress("UnsafeCallOnNullableType")
    private fun getParameterizedPrimitiveForgery(
        arguments: List<KTypeProjection>,
        classifier: KClass<*>,
        klass: KClass<*>,
        forge: Forge,
        forgery: Forge.() -> Any?
    ): Any? {
        return when (classifier) {
            in knownLists -> forge.aList { getPrimitiveForgery(arguments[0].type!!, klass, forge, forgery) }
            in knownSets -> forge.aList { getPrimitiveForgery(arguments[0].type!!, klass, forge, forgery) }.toSet()
            else -> null
        }
    }

    private fun uninjectable(property: KMutableProperty<*>): Nothing {
        throw ForgeryInjectorException("Unable to forge $property.")
    }

    private class KMapProperty<R>(
        override val returnType: KType
    ) : KMutableProperty<R> {
        override val annotations: List<Annotation>
            get() = TODO("Not yet implemented")
        override val getter: KProperty.Getter<R>
            get() = TODO("Not yet implemented")
        override val isAbstract: Boolean
            get() = TODO("Not yet implemented")
        override val isConst: Boolean
            get() = TODO("Not yet implemented")
        override val isFinal: Boolean
            get() = TODO("Not yet implemented")
        override val isLateinit: Boolean
            get() = TODO("Not yet implemented")
        override val isOpen: Boolean
            get() = TODO("Not yet implemented")
        override val isSuspend: Boolean
            get() = TODO("Not yet implemented")
        override val name: String
            get() = TODO("Not yet implemented")
        override val parameters: List<KParameter>
            get() = TODO("Not yet implemented")
        override val setter: KMutableProperty.Setter<R>
            get() = TODO("Not yet implemented")
        override val typeParameters: List<KTypeParameter>
            get() = TODO("Not yet implemented")
        override val visibility: KVisibility?
            get() = TODO("Not yet implemented")

        override fun call(vararg args: Any?): R {
            TODO("Not yet implemented")
        }

        override fun callBy(args: Map<KParameter, Any?>): R {
            TODO("Not yet implemented")
        }
    }

    // endregion

    companion object {
        private val knownLists = setOf<KClass<*>>(
            List::class, Collection::class
        )
        private val knownSets = setOf<KClass<*>>(
            Set::class
        )
        private val knownMaps = setOf<KClass<*>>(
            Map::class
        )
    }
}
