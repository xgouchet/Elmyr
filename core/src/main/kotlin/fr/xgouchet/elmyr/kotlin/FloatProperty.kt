package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forge
import kotlin.properties.ReadOnlyProperty

/**
 * This class implements a [ReadOnlyProperty], forging a random float within a given range.
 *
 * @param min the minimum value (inclusive), default = -Float#MAX_VALUE
 * @param max the maximum value (exclusive), default = Float#MAX_VALUE
 */
class FloatProperty(
    private val min: Float = -Float.MAX_VALUE,
    private val max: Float = Float.MAX_VALUE
) : ForgeryProperty<Float>() {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): Float {
        return forge.aFloat(min, max)
    }

    companion object {

        /**
         * Creates a [ReadOnlyProperty] that will forge a random float within the given range.
         *
         * @param min the minimum value (inclusive), default = -Float#MAX_VALUE
         * @param max the maximum value (exclusive), default = Float#MAX_VALUE
         */
        fun floatForgery(
            min: Float = -Float.MAX_VALUE,
            max: Float = Float.MAX_VALUE
        ): ForgeryProperty<Float> = FloatProperty(min, max)
    }
}
