package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forge
import kotlin.properties.ReadOnlyProperty

/**
 * This class implements a [ReadOnlyProperty], forging a random boolean with a given probability of being true.
 *
 * @param probability the probability the boolean will be true (default 0.5f)
 */
class BooleanProperty(
    private val probability: Float = Forge.HALF_PROBABILITY
) : ForgeryProperty<Boolean>() {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): Boolean {
        return forge.aBool(probability)
    }

    companion object {
        /**
         * Creates a [ReadOnlyProperty] that will forge a random boolean with the given probability of being true.
         * @param probability the probability the boolean will be true (default 0.5f)
         */
        fun booleanForgery(
            probability: Float = Forge.HALF_PROBABILITY
        ): ForgeryProperty<Boolean> = BooleanProperty(probability)
    }
}
