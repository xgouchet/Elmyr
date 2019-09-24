package fr.xgouchet.elmyr.inject.reflect

import java.lang.reflect.Field

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal fun Field.setPrivate(
    target: Any,
    value: Any?
) {
    val wasAccessible = isAccessible

    // make field accessible
    try {
        isAccessible = true
    } catch (t: Throwable) {
        // Ignore
    }

    // Set the field value
    try {
        set(target, value)
    } catch (e: IllegalAccessException) {
        throw IllegalStateException("Access not authorized on field '$this' of object '$target' " +
                "with value: '$value'", e)
    } catch (e: IllegalArgumentException) {
        throw IllegalStateException("Wrong argument on field '$this' of object '$target' " +
                "with value: '$value', \nreason : ${e.message}", e)
    }

    // reset accessibility
    try {
        isAccessible = wasAccessible
    } catch (t: Throwable) {
        // Ignore
    }
}