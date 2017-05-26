package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Case
import fr.xgouchet.elmyr.Forger
import fr.xgouchet.elmyr.StringConstraint

/**
 * A read-only String property
 *
 * @author Xavier F. Gouchet
 */
class ForgedString(
        val constraint: StringConstraint = StringConstraint.ANY,
        val case: Case = Case.ANY,
        val size: Int = -1,
        val regex: Regex? = null,
        forger: Forger)
    : ForgedProperty<String>(forger) {

    override fun generate(forger: Forger): String {
        if (regex != null) {
            return forger.aStringMatching(regex)
        } else {
            return forger.aString(constraint, case, size)
        }
    }


}