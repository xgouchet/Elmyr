package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forge
import kotlin.properties.ReadOnlyProperty

/**
 * This class implements a [ReadOnlyProperty], forging a random double within a given range.
 *
 * @property min the minimum value (inclusive), default = -Double#MAX_VALUE
 * @property max the maximum value (exclusive), default = Double#MAX_VALUE
 */
class DoubleProperty(
    private val min: Double = -Double.MAX_VALUE,
    private val max: Double = Double.MAX_VALUE
) : ForgeryProperty<Double>() {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): Double {
        return forge.aDouble(min, max)
    }

    companion object {

        /**
         * Creates a [ReadOnlyProperty] that will forge a random double within the given range.
         *
         * @param min the minimum value (inclusive), default = -Double#MAX_VALUE
         * @param max the maximum value (exclusive), default = Double#MAX_VALUE
         */
        fun doubleForgery(
            min: Double = -Double.MAX_VALUE,
            max: Double = Double.MAX_VALUE
        ): ForgeryProperty<Double> = DoubleProperty(min, max)
    }
}
