package fr.xgouchet.elmyr.junit4.internal.reflect

import java.lang.IllegalStateException
import kotlin.reflect.KMutableProperty
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaSetter

internal fun <T> KMutableProperty<T>.setPrivate(target: Any, value: Any?) {
    val setter = javaSetter
    val field = javaField

    if (setter != null) {
        setter.invokePrivate(target, value)
    } else if (field != null) {
        field.setPrivate(target, value)
    } else {
        throw IllegalStateException("Access not possible on property '$this' of object '$target' " +
                "with value: '$value'")
    }
}
