package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * Represents a single char to be matched
 * @author Xavier F. Gouchet
 */
class RawChar(val rawChar: Char, parent: RegexParentNode) : RegexNode(parent) {

    override fun buildIteration(forger: Forger, builder: StringBuilder) {
        builder.append(rawChar)
    }

    override fun describe(builder: StringBuilder) {
        builder.append(rawChar)
    }

}