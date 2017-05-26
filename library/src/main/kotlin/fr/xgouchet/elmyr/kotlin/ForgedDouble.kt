package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.DoubleConstraint
import fr.xgouchet.elmyr.Forger

/**
 * A read-only Double property
 *
 * @author Xavier F. Gouchet
 */
class ForgedDouble(
        val constraint: DoubleConstraint = DoubleConstraint.ANY,
        val min: Double = Double.NEGATIVE_INFINITY,
        val max: Double = Double.POSITIVE_INFINITY,
        val mean: Double = 0.0,
        val standardDeviation: Double = -1.0,
        forger: Forger)
    : ForgedProperty<Double>(forger) {

    override fun generate(forger: Forger): Double {
        if (standardDeviation >= 0) {
            return forger.aGaussianDouble(mean, standardDeviation)
        } else if ((min != Double.NEGATIVE_INFINITY) and (max != Double.POSITIVE_INFINITY)) {
            return forger.aDouble(min, max)
        } else {
            return forger.aDouble(constraint)
        }
    }

}

