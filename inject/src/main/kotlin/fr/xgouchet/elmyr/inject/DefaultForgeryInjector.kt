package fr.xgouchet.elmyr.inject

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.inject.reflect.invokePrivate
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
                .filter { it.annotations.any { annotation -> annotation is Forgery } }
        when (invalidProperties.size) {
            0 -> injectInClassSafe(clazz, forge, target)
            1 -> throw ForgeryInjectorException(target, invalidProperties.first())
            else -> throw ForgeryInjectorException(target, invalidProperties)
        }
    }

    private fun injectInClassSafe(clazz: KClass<*>, forge: Forge, target: Any) {
        val mutableProperties = clazz.declaredMembers.filterIsInstance<KMutableProperty<*>>()

        for (property in mutableProperties) {
            injectInProperty(forge, property, target)
        }
    }

    private fun injectInProperty(
        forge: Forge,
        property: KMutableProperty<*>,
        target: Any
    ) {
        for (annotation in property.annotations) {
            if (annotation is Forgery) {
                processPropertyWithForgery(forge, property, target)
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun processPropertyWithForgery(
        forge: Forge,
        property: KMutableProperty<*>,
        target: Any
    ) {
        val forgery = forgeProperty(property.returnType, forge)
                ?: throw ForgeryInjectorException("Unable to forge $property.")

        property.setter.invokePrivate(target, forgery)
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
