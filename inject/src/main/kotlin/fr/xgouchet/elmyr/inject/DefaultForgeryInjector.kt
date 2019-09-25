package fr.xgouchet.elmyr.inject

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.inject.reflect.invokePrivate
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
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
        @Suppress("UnsafeCast")
        val kclass = (property.returnType.classifier as KClass<*>)
        val forgery = forge.getForgery(kclass.java)
        property.setter.invokePrivate(target, forgery)
    }

    // endregion
}