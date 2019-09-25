package fr.xgouchet.elmyr.inject

import fr.xgouchet.elmyr.ForgeryException
import kotlin.reflect.KProperty

/**
 * Raised by Elmyr to emit an error preventing a [ForgeryInjector] to inject a forgery.
 *
 * @param message the detail message (which is saved for later retrieval by the [Throwable.message] property).
 */
class ForgeryInjectorException(
    message: String
) : ForgeryException(message) {

    constructor(
        target: Any,
        property: KProperty<*>
    ) : this("Impossible to inject forgery on property " +
            "'${target.javaClass.canonicalName}.${property.name}'")

    constructor(
        target: Any,
        properties: List<KProperty<*>>
    ) : this("Impossible to inject forgery in class " +
            "'${target.javaClass.canonicalName}' on the following properties : " +
            properties.joinToString { it.name })
}
