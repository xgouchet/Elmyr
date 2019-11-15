package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.FloatConstraint
import fr.xgouchet.elmyr.Forger

/**
 * A read-only Float property
 *
 * @author Xavier F. Gouchet
 */
class ForgedFloat(
    val constraint: FloatConstraint = FloatConstraint.ANY,
    val min: Float = Float.NEGATIVE_INFINITY,
    val max: Float = Float.POSITIVE_INFINITY,
    val mean: Float = 0f,
    val standardDeviation: Float = -1f,
    forger: Forger
) :
    ForgedProperty<Float>(forger) {

    override fun generate(forger: Forger): Float {
        return if (standardDeviation >= 0) {
            forger.aGaussianFloat(mean, standardDeviation)
        } else if ((min != Float.NEGATIVE_INFINITY) and (max != Float.POSITIVE_INFINITY)) {
            forger.aFloat(min, max)
        } else {
            forger.aFloat(constraint)
        }
    }
}
