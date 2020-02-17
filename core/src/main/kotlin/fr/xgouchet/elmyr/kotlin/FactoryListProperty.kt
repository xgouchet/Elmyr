package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forge
import kotlin.properties.ReadOnlyProperty

/**
 * This class implements a [ReadOnlyProperty], forging a random list of object with a factory provided to the forge.
 *
 * @param T the type of the object to forge
 * @param clazz the class of the type to be forged
 */
class FactoryListProperty<T : Any>(
    private val clazz: Class<T>
) : ForgeryProperty<List<T>>() {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): List<T> {
        return forge.aList { getForgery(clazz) }
    }

    companion object {

        /**
         * Creates a [ReadOnlyProperty] that will forge a random list of object with a factory provided to the [Forge].
         *
         * @param T the type of the object to forge
         */
        inline fun <reified T : Any> factoryListForgery():
            ForgeryProperty<List<T>> = FactoryListProperty(T::class.java)
    }
}
