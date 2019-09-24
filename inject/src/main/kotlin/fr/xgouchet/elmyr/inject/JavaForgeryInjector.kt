package fr.xgouchet.elmyr.inject

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.inject.reflect.setPrivate
import java.lang.reflect.Field

internal class JavaForgeryInjector : ForgeryInjector {

    // region ForgeryInjector

    override fun inject(forge: Forge, target: Any) {
        var classToProcess: Class<*> = target.javaClass
        while (classToProcess != Any::class.java) {
            injectInClass(forge, classToProcess, target)
            classToProcess = classToProcess.superclass
        }
    }

    // endregion

    // region Internal

    private fun injectInClass(
        forge: Forge,
        clazz: Class<*>,
        target: Any
    ) {
        val fields = clazz.declaredFields
        for (field in fields) {
            injectInField(forge, field, target)
        }
    }

    private fun injectInField(
        forge: Forge,
        field: Field,
        target: Any
    ) {
        for (annotation in field.annotations) {
            if (annotation is Forgery) {
                injectInForgeryField(forge, field, target, annotation)
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun injectInForgeryField(
        forge: Forge,
        field: Field,
        target: Any,
        annotation: Forgery
    ) {
        val forgery = forge.getForgery(field.type)
        if (forgery != null) {
            try {
                field.setPrivate(target, forgery)
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