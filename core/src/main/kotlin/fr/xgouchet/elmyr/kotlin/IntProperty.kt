package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forge
import kotlin.properties.ReadOnlyProperty

/**
 * This class implements a [ReadOnlyProperty], forging a random int within a given range.
 *
 * @param min the minimum value (inclusive), default = Int#MIN_VALUE
 * @param max the maximum value (exclusive), default = Int#MAX_VALUE
 */
class IntProperty(
    private val min: Int = Int.MIN_VALUE,
    private val max: Int = Int.MAX_VALUE
) : ForgeryProperty<Int>() {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): Int {
        return forge.anInt(min, max)
    }

    companion object {

        /**
         * Creates a [ReadOnlyProperty] that will forge a random int within the given range.
         *
         * @param min the minimum value (inclusive), default = Int#MIN_VALUE
         * @param max the maximum value (exclusive), default = Int#MAX_VALUE
         */
        fun intForgery(
            min: Int = Int.MIN_VALUE,
            max: Int = Int.MAX_VALUE
        ): ForgeryProperty<Int> = IntProperty(min, max)
    }
}
