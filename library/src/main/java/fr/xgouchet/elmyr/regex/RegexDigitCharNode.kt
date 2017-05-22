package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * Represents a digit character
 *
 * @author Xavier F. Gouchet
 */
class RegexDigitCharNode(parent: RegexParentNode) : RegexNode(parent) {

    override fun buildIteration(forger: Forger, builder: StringBuilder) {
        builder.append(forger.aDigitChar())
    }

    override fun describe(builder: StringBuilder) {
        builder.append("\\d")
    }

}