package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forger
import fr.xgouchet.elmyr.IntConstraint

/**
 * A read-only Int property
 *
 * @author Xavier F. Gouchet
 */
class ForgedInt(
        val constraint: IntConstraint = IntConstraint.ANY,
        val min: Int = -1,
        val max: Int = -1,
        forger: Forger)
    : ForgedProperty<Int>(forger) {

    override fun generate(forger: Forger): Int {
        if ((min < 0) and (max < 0)) {
            return forger.anInt(constraint)
        } else {
            return forger.anInt(min, max)
        }
    }

}

