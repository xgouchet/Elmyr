package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * Represents a digit character
 *
 * @author Xavier F. Gouchet
 */
class RegexNonDigitCharNode(parent: RegexParentNode)
    : RegexNode(parent) {

    override fun buildIteration(forger: Forger, builder: StringBuilder) {
        builder.append(forger.aNonNumericalChar())
    }

    override fun describe(builder: StringBuilder) {
        builder.append("\\D")
    }

}