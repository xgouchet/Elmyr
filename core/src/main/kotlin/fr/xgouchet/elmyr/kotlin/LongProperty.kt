package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forge
import kotlin.properties.ReadOnlyProperty

/**
 * This class implements a [ReadOnlyProperty], forging a random long within a given range.
 *
 * @property min the minimum value (inclusive), default = Long#MIN_VALUE
 * @property max the maximum value (exclusive), default = Long#MAX_VALUE
 */
class LongProperty(
    private val min: Long = Long.MIN_VALUE,
    private val max: Long = Long.MAX_VALUE
) : ForgeryProperty<Long>() {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): Long {
        return forge.aLong(min, max)
    }

    companion object {

        /**
         * Creates a [ReadOnlyProperty] that will forge a random long within the given range.
         *
         * @param min the minimum value (inclusive), default = Long#MIN_VALUE
         * @param max the maximum value (exclusive), default = Long#MAX_VALUE
         */
        fun longForgery(
            min: Long = Long.MIN_VALUE,
            max: Long = Long.MAX_VALUE
        ): ForgeryProperty<Long> = LongProperty(min, max)
    }
}
