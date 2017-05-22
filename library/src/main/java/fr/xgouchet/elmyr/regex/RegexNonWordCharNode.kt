package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * Represents a non word character
 *
 * @author Xavier F. Gouchet
 */
class RegexNonWordCharNode(parent: RegexParentNode)
    : RegexNode(parent) {

    override fun buildIteration(forger: Forger, builder: StringBuilder) {
        builder.append(forger.aNonAlphaNumChar())
    }

    override fun describe(builder: StringBuilder) {
        builder.append("\\W")
    }

}