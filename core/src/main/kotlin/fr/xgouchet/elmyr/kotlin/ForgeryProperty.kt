package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryAware
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * This is the base for all forgery based property. It must be applied on a [ForgeryAware] instance,
 * and will keep returning the same instance until the forge's seed changes.
 *
 * @param T the type of the property
 */
abstract class ForgeryProperty<T> :
        ReadOnlyProperty<ForgeryAware, T> {

    private var lastSeed: Long = 0
    private var memoizedValue: T? = null

    /** @inheritdoc */
    final override fun getValue(thisRef: ForgeryAware, property: KProperty<*>): T {
        val forge = thisRef.forge
        val lastValue = memoizedValue
        return if (forge.seed == lastSeed && lastValue != null) {
            lastValue
        } else {
            lastSeed = forge.seed
            val value = getForgery(forge)
            memoizedValue = value
            value
        }
    }

    /**
     * @param forge the [Forge] to use to get the forgery instance
     * @return a forged instance of the desired type
     */
    abstract fun getForgery(forge: Forge): T
}
