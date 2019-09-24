package fr.xgouchet.elmyr.inject.reflect

import java.lang.reflect.Method

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal fun Method.invokePrivate(target: Any?, vararg params: Any?) {
    val wasAccessible = isAccessible

    // make field accessible
    try {
        isAccessible = true
    } catch (t: Throwable) {
        // Ignore
    }

    // Set the field value
    try {
        @Suppress("SpreadOperator")
        invoke(target, *params)
    } catch (e: IllegalAccessException) {
        throw IllegalStateException("Access not authorized on method '$this' of object '$target' " +
                "with parameters: $params", e)
    } catch (e: IllegalArgumentException) {
        throw IllegalStateException("Wrong argument on method '$this' of object '$target' " +
                "with paramaeters: $params, \nreason : ${e.message}", e)
    }

    // reset accessibility
    try {
        isAccessible = wasAccessible
    } catch (t: Throwable) {
        // Ignore
    }
}