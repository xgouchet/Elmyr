package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Case
import fr.xgouchet.elmyr.CharConstraint
import fr.xgouchet.elmyr.Forger

/**
 * A read-only Char property
 *
 * @author Xavier F. Gouchet
 */
class ForgedChar(
    val constraint: CharConstraint = CharConstraint.ANY,
    val case: Case = Case.ANY,
    forger: Forger
) :
    ForgedProperty<Char>(forger) {

    override fun generate(forger: Forger): Char {
        return forger.aChar(constraint, case)
    }
}
