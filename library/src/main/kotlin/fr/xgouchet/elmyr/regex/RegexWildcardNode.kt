package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.CharConstraint
import fr.xgouchet.elmyr.Forger

/**
 * Represents a wildcard in a regex (ie : ‘.’)
 *
 * @author Xavier F. Gouchet
 */
class RegexWildcardNode(parent: RegexParentNode? = null)
    : RegexNode(parent) {

    override fun buildIteration(forger: Forger, builder: StringBuilder) {
        builder.append(forger.aChar(CharConstraint.ANY))
    }

    override fun describe(builder: StringBuilder) {
        builder.append(".")
    }
}
