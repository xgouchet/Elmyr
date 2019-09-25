package fr.xgouchet.elmyr

/**
 * Raised by a [Forge] when asked to generate an instance with no compatible [ForgeryFactory].
 *
 * @param clazz the class that was being forged
 */
class ForgeryFactoryMissingException(
    clazz: Class<*>
) : ForgeryException("Cannot create forgery for type ${clazz.canonicalName}.\n" +
        "Make sure you provide a factory for this type.")