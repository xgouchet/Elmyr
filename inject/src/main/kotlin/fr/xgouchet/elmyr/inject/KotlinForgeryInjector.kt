package fr.xgouchet.elmyr.inject

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.inject.reflect.setPrivate
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

internal class KotlinForgeryInjector : ForgeryInjector {

    // region ForgeryInjector

    override fun inject(forge: Forge, target: Any) {
        val classesToProcess = mutableSetOf<KClass<*>>()
        classesToProcess.add(target.javaClass.kotlin)

        while (classesToProcess.isNotEmpty()) {
            val classToProcess = classesToProcess.first()
            if (classToProcess != Any::class) {
                injectInClass(forge, classToProcess, target)
                classToProcess.supertypes.forEach {
                    classesToProcess.add(it.javaClass.kotlin)
                }
                classesToProcess.remove(classToProcess)
            }
        }
    }

    // endregion

    // region Internal

    private fun injectInClass(
        forge: Forge,
        clazz: KClass<*>,
        target: Any
    ) {
        val properties = clazz.members.filterIsInstance<KMutableProperty<*>>()

        for (property in properties) {
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
                processPropertyWithForgery(forge, property, target, annotation)
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun processPropertyWithForgery(
        forge: Forge,
        property: KMutableProperty<*>,
        target: Any,
        annotation: Annotation?
    ) {
        @Suppress("UnsafeCast")
        val kclass = (property.returnType.classifier as KClass<*>)
        val forgery = forge.getForgery(kclass.java)
        try {
            property.setPrivate(target, forgery)
        } catch (e: Exception) {
            throw IllegalStateException(
                    "Problems setting field ${property.name} annotated with $annotation",
                    e
            )
        }
    }

    // endregion
}