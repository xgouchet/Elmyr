package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forge
import kotlin.properties.ReadOnlyProperty

/**
 * This class implements a [ReadOnlyProperty], forging a random object with a factory provided to the forge.
 *
 * @param T the type of the object to forge
 * @param clazz the class of the type to be forged
 */
class FactoryProperty<T : Any>(
    private val clazz: Class<T>
) : ForgeryProperty<T>() {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): T {
        return forge.getForgery(clazz)
    }
}

/**
 * Creates a [ReadOnlyProperty] that will forge a random object with a factory provided to the [Forge].
 *
 * @param T the type of the object to forge
 */
inline fun <reified T : Any> factoryForgery(): ForgeryProperty<T> = FactoryProperty(T::class.java)
