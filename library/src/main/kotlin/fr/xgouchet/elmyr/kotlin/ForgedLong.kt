package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forger
import fr.xgouchet.elmyr.LongConstraint

/**
 * A read-only Long property
 *
 * @author Xavier F. Gouchet
 */
class ForgedLong(
        val constraint: LongConstraint = LongConstraint.ANY,
        val min: Long = -1,
        val max: Long = -1,
        forger: Forger)
    : ForgedProperty<Long>(forger) {

    override fun generate(forger: Forger): Long {
        if ((min < 0) and (max < 0)) {
            return forger.aLong(constraint)
        } else {
            return forger.aLong(min, max)
        }
    }

}
