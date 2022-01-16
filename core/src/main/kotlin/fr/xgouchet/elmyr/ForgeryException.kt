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
