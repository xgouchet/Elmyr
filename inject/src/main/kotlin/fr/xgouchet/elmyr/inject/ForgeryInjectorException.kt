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

    companion object {

        internal fun withProperty(
            target: Any,
            property: KProperty<*>
        ) = ForgeryInjectorException(
                "Impossible to inject forgery on property " +
                        "'${target.javaClass.canonicalName}.${property.name}'"
        )

        internal fun withProperties(
            target: Any,
            properties: List<KProperty<*>>
        ) = ForgeryInjectorException(
                "Impossible to inject forgery in class " +
                        "'${target.javaClass.canonicalName}' on the following properties : " +
                        properties.joinToString { it.name }
        )

        internal fun withErrors(
            target: Any,
            throwables: List<Throwable>
        ) = ForgeryInjectorException(
                "Impossible to inject forgery in class " +
                        "'${target.javaClass.canonicalName}' because of the following errors :\n\t" +
                        throwables.joinToString("\n\t") { "${it.javaClass.canonicalName}: ${it.message} ${it.stackTrace.joinToString("\n\t\t")}" }
        )
    }
}
