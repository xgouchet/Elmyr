package fr.xgouchet.elmyr.junit4.internal.engine

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.junit4.ForgeAnnotationEngine
import fr.xgouchet.elmyr.junit4.Forgery
import fr.xgouchet.elmyr.junit4.internal.reflect.setPrivate
import java.lang.reflect.Field

internal class JavaAnnotationEngine(
    private val forge: Forge
) : ForgeAnnotationEngine {

    // region ForgeAnnotationEngine
    override fun process(testInstance: Any) {
        processWithAnnotations(testInstance.javaClass, testInstance)
    }

    // endregion

    // region Internal

    private fun processWithAnnotations(clazz: Class<*>, testInstance: Any) {
        var classContext = clazz
        while (classContext != Any::class.java) {
            processClassWithAnnotations(classContext, testInstance)
            classContext = classContext.superclass
        }
    }

    private fun processClassWithAnnotations(clazz: Class<*>, testInstance: Any) {
        val fields = clazz.declaredFields
        for (field in fields) {
            processFieldWithAnnotation(field, testInstance)
        }
    }

    private fun processFieldWithAnnotation(field: Field, testInstance: Any) {
        for (annotation in field.annotations) {
            if (annotation is Forgery) {
                processFieldWithForgery(field, testInstance, annotation)
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun processFieldWithForgery(
        field: Field,
        testInstance: Any,
        annotation: Annotation?
    ) {
        val forgery = forge.getForgery(field.type)
        if (forgery != null) {
            try {
                field.setPrivate(testInstance, forgery)
            } catch (e: Exception) {
                throw IllegalStateException(
                        "Problems setting field ${field.name} annotated with $annotation",
                        e
                )
            }
        }
    }

    // endregion
}