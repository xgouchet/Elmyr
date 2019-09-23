package fr.xgouchet.elmyr.junit4.internal.engine

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.junit4.ForgeAnnotationEngine
import fr.xgouchet.elmyr.junit4.Forgery
import fr.xgouchet.elmyr.junit4.internal.reflect.setPrivate
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

internal class KotlinAnnotationEngine(
    private val forge: Forge
) : ForgeAnnotationEngine {

    // region ForgeAnnotationEngine
    override fun process(testInstance: Any) {
        processWithAnnotations(testInstance.javaClass.kotlin, testInstance)
    }

    // endregion

    // region Internal

    private fun processWithAnnotations(clazz: KClass<*>, testInstance: Any) {
        val classesToProcess = mutableSetOf<KClass<*>>()
        classesToProcess.add(clazz)

        while (classesToProcess.isNotEmpty()) {
            val classToProcess = classesToProcess.first()
            if (classToProcess != Any::class) {
                processClassWithAnnotations(classToProcess, testInstance)
                classToProcess.supertypes.forEach {
                    classesToProcess.add(it.javaClass.kotlin)
                }
                classesToProcess.remove(classToProcess)
            }
        }
    }

    private fun processClassWithAnnotations(clazz: KClass<*>, testInstance: Any) {
        val properties = clazz.members.filterIsInstance<KMutableProperty<*>>()

        for (property in properties) {
            processPropertyWithAnnotation(property, testInstance)
        }
    }

    private fun processPropertyWithAnnotation(property: KMutableProperty<*>, testInstance: Any) {
        for (annotation in property.annotations) {
            if (annotation is Forgery) {
                processPropertyWithForgery(property, testInstance, annotation)
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun processPropertyWithForgery(
        property: KMutableProperty<*>,
        testInstance: Any,
        annotation: Annotation?
    ) {
        @Suppress("UnsafeCast")
        val kclass = (property.returnType.classifier as KClass<*>)
        val forgery = forge.getForgery(kclass.java)
        if (forgery != null) {
            try {
                property.setPrivate(testInstance, forgery)
            } catch (e: Exception) {
                throw IllegalStateException(
                        "Problems setting field ${property.name} annotated with $annotation",
                        e
                )
            }
        }
    }

    // endregion
}