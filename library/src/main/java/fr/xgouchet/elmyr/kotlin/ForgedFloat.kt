package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.FloatConstraint
import fr.xgouchet.elmyr.Forger
import fr.xgouchet.elmyr.IntConstraint
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

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
        forger: Forger)
    : ForgedProperty<Float>(forger) {

    override fun generate(forger: Forger): Float {
        if (standardDeviation >= 0) {
            return forger.aProbalisticFloat(mean, standardDeviation)
        } else if ((min != Float.NEGATIVE_INFINITY) and (max != Float.POSITIVE_INFINITY)) {
            return forger.aFloat(min, max)
        } else {
            return forger.aFloat(constraint)
        }
    }

}

