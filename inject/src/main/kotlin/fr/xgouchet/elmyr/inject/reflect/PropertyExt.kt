package fr.xgouchet.elmyr.inject.reflect

import kotlin.reflect.KMutableProperty
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaSetter

internal fun <T> KMutableProperty<T>.setPrivate(target: Any, value: Any?) {
    val setter = javaSetter
    val field = javaField

    when {
        setter != null -> setter.invokePrivate(target, value)
        field != null -> field.setPrivate(target, value)
        else -> throw IllegalStateException(
                "Access not possible on property '$this' of object '$target' " +
                        "with value: '$value'")
    }
}
