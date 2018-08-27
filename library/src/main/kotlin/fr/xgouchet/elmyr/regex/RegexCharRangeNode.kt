package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * Represents a character range in a choice (eg : in the regex “[a-z_-]” this would represent the range from ‘a’ to ‘z’
 *
 * @author Xavier F. Gouchet
 */
internal class RegexCharRangeNode(val from: Char,
                                  val to: Char,
                                  parent: RegexParentNode)
    : RegexNode(parent) {

    override fun buildIteration(forger: Forger, builder: StringBuilder) {
        builder.append(forger.aChar(from, to))
    }

    override fun describe(builder: StringBuilder) {
        builder.append(from)
                .append("-")
                .append(to)
    }

}
