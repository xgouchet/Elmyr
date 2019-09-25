package fr.xgouchet.elmyr.inject.reflect

import kotlin.reflect.KMutableProperty
import kotlin.reflect.jvm.isAccessible

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal fun KMutableProperty.Setter<*>.invokePrivate(target: Any?, vararg params: Any?) {
    val wasAccessible = this.isAccessible

    // make field accessible
    isAccessible = true

    // Set the field value
    @Suppress("SpreadOperator")
    this.call(target, *params)

    // reset accessibility
    isAccessible = wasAccessible
}