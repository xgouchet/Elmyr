package fr.xgouchet.elmyr

/**
 * Raised by Elmyr to emit an error preventing a [Forge] to forge a forgery.
 *
 * @param message the detail message (which is saved for later retrieval by the [Throwable.message] property).
 * @param cause the cause (which is saved for later retrieval by the [Throwable.cause] property).
 */
open class ForgeryException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * Raised by a [Forge] when asked to forge an instance with no compatible [ForgeryFactory].
 *
 * @param clazz the class that was being forged
 */
class ForgeryFactoryMissingException(
    clazz: Class<*>
) : ForgeryException(
    "Cannot create forgery for type ${clazz.canonicalName}.\n" +
        "Make sure you provide a factory for this type."
)

/**
 * Raised by a [Forge] when asked to forge an instance with no compatible [ForgeryFactory].
 *
 * @param clazz the class that was being forged
 * @param message the message about the reflexive issue preventing the forgery
 */
class ReflexiveForgeryFactoryException(
    clazz: Class<*>,
    message: String?
) : ForgeryException(
    "Cannot create an automatic forgery for type ${clazz.canonicalName}: $message.\n" +
        "You can try providing a factory for this type."
)
