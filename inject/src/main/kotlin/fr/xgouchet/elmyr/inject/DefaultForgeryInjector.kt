package fr.xgouchet.elmyr.inject

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.BoolForgery
import fr.xgouchet.elmyr.annotation.DoubleForgery
import fr.xgouchet.elmyr.annotation.FloatForgery
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.annotation.LongForgery
import fr.xgouchet.elmyr.annotation.RegexForgery
import fr.xgouchet.elmyr.annotation.StringForgery
import fr.xgouchet.elmyr.annotation.StringForgeryType
import fr.xgouchet.elmyr.inject.reflect.invokePrivate
import java.lang.IllegalStateException
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
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
                        annotation is RegexForgery
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
        for (annotation in property.annotations) {
            when (annotation) {
                is Forgery -> processPropertyWithForgery(forge, property, target)
                is BoolForgery -> processPropertyWithBooleanForgery(forge, annotation, property, target)
                is IntForgery -> processPropertyWithIntForgery(forge, annotation, property, target)
                is LongForgery -> processPropertyWithLongForgery(forge, annotation, property, target)
                is FloatForgery -> processPropertyWithFloatForgery(forge, annotation, property, target)
                is DoubleForgery -> processPropertyWithDoubleForgery(forge, annotation, property, target)
                is StringForgery -> processPropertyWithStringForgery(forge, annotation, property, target)
                is RegexForgery -> processPropertyWithRegexForgery(forge, annotation, property, target)
            }
        }
    }

    private fun processPropertyWithForgery(
        forge: Forge,
        property: KMutableProperty<*>,
        target: Any
    ) {
        val forgery = forgeProperty(property.returnType, forge)
            ?: throw ForgeryInjectorException("Unable to forge $property.")

        property.setter.invokePrivate(target, forgery)
    }

    private fun processPropertyWithBooleanForgery(
        forge: Forge,
        annotation: BoolForgery,
        property: KMutableProperty<*>,
        target: Any
    ) {
        check(annotation.probability >= 0f) {
            "You can only use an BoolForgery with a probability between 0f and 1f"
        }
        check(annotation.probability <= 1f) {
            "You can only use an BoolForgery with a probability between 0f and 1f"
        }
        val forgery: Forge.() -> Boolean = { aBool(annotation.probability) }
        property.setter.invokePrivate(
            target,
            forgePrimitiveProperty(property.returnType, Boolean::class, forge, forgery)
        )
    }

    private fun processPropertyWithIntForgery(
        forge: Forge,
        annotation: IntForgery,
        property: KMutableProperty<*>,
        target: Any
    ) {
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
        property.setter.invokePrivate(
            target,
            forgePrimitiveProperty(property.returnType, Int::class, forge, forgery)
        )
    }

    private fun processPropertyWithLongForgery(
        forge: Forge,
        annotation: LongForgery,
        property: KMutableProperty<*>,
        target: Any
    ) {
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
        property.setter.invokePrivate(
            target,
            forgePrimitiveProperty(property.returnType, Long::class, forge, forgery)
        )
    }

    private fun processPropertyWithFloatForgery(
        forge: Forge,
        annotation: FloatForgery,
        property: KMutableProperty<*>,
        target: Any
    ) {
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

        property.setter.invokePrivate(
            target,
            forgePrimitiveProperty(property.returnType, Float::class, forge, forgery)
        )
    }

    private fun processPropertyWithDoubleForgery(
        forge: Forge,
        annotation: DoubleForgery,
        property: KMutableProperty<*>,
        target: Any
    ) {
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

        property.setter.invokePrivate(
            target,
            forgePrimitiveProperty(property.returnType, Double::class, forge, forgery)
        )
    }

    private fun processPropertyWithStringForgery(
        forge: Forge,
        annotation: StringForgery,
        property: KMutableProperty<*>,
        target: Any
    ) {
        val forgery: Forge.() -> String = when (annotation.value) {
            StringForgeryType.ALPHABETICAL -> { -> anAlphabeticalString(annotation.case) }
            StringForgeryType.ALPHA_NUMERICAL -> { -> anAlphaNumericalString(annotation.case) }
            StringForgeryType.NUMERICAL -> { -> aNumericalString() }
            StringForgeryType.HEXADECIMAL -> { -> anHexadecimalString(annotation.case) }
            StringForgeryType.WHITESPACE -> { -> aWhitespaceString() }
            StringForgeryType.ASCII -> { -> anAsciiString() }
            StringForgeryType.ASCII_EXTENDED -> { -> anExtendedAsciiString() }
        }
        property.setter.invokePrivate(
            target,
            forgePrimitiveProperty(property.returnType, String::class, forge, forgery)
        )
    }

    private fun processPropertyWithRegexForgery(
        forge: Forge,
        annotation: RegexForgery,
        property: KMutableProperty<*>,
        target: Any
    ) {
        val forgery: Forge.() -> String = { aStringMatching(annotation.value) }
        property.setter.invokePrivate(
            target,
            forgePrimitiveProperty(property.returnType, String::class, forge, forgery)
        )
    }

    private fun forgeProperty(type: KType, forge: Forge): Any? {
        val arguments = type.arguments
        val classifier = type.classifier
        if (classifier !is KClass<*>) return null

        return if (arguments.isEmpty()) {
            forge.getForgery(classifier.java)
        } else {
            forgeParameterizedProperty(forge, arguments, classifier)
        }
    }

    @Suppress("UnsafeCallOnNullableType")
    private fun forgeParameterizedProperty(
        forge: Forge,
        arguments: List<KTypeProjection>,
        classifier: KClass<*>
    ): Any? {
        return when (classifier) {
            in knownLists -> forge.aList { forgeProperty(arguments[0].type!!, forge) }
            in knownSets -> forge.aList { forgeProperty(arguments[0].type!!, forge) }.toSet()
            in knownMaps -> forge.aMap {
                val key = forgeProperty(arguments[0].type!!, forge)
                val value = forgeProperty(arguments[1].type!!, forge)
                key to value
            }
            else -> {
                null
            }
        }
    }

    private fun forgePrimitiveProperty(
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
            forgeParameterizedPrimitiveProperty(arguments, classifier, klass, forge, forgery)
        }
    }

    @Suppress("UnsafeCallOnNullableType")
    private fun forgeParameterizedPrimitiveProperty(
        arguments: List<KTypeProjection>,
        classifier: KClass<*>,
        klass: KClass<*>,
        forge: Forge,
        forgery: Forge.() -> Any?
    ): Any? {
        return when (classifier) {
            in knownLists -> forge.aList { forgePrimitiveProperty(arguments[0].type!!, klass, forge, forgery) }
            in knownSets -> forge.aList { forgePrimitiveProperty(arguments[0].type!!, klass, forge, forgery) }.toSet()
            else -> {
                null
            }
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
