package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forge
import kotlin.properties.ReadOnlyProperty

/**
 * This class implements a [ReadOnlyProperty], forging a random set of object with a factory provided to the forge.
 *
 * @param T the type of the object to forge
 * @param clazz the class of the type to be forged
 */
class FactorySetProperty<T : Any>(
    private val clazz: Class<T>
) : ForgeryProperty<Set<T>>() {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): Set<T> {
        return forge.aList { getForgery(clazz) }.toSet()
    }

    companion object {

        /**
         * Creates a [ReadOnlyProperty] that will forge a random set of object with a factory provided to the [Forge].
         *
         * @param T the type of the object to forge
         */
        inline fun <reified T : Any> factorySetForgery():
            ForgeryProperty<Set<T>> = FactorySetProperty(T::class.java)
    }
}
