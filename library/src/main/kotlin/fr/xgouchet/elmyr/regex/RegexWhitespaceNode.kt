package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * Represents a whitespace character (ie : ‘\s’)
 *
 * @author Xavier F. Gouchet
 */
class RegexWhitespaceNode(parent: RegexParentNode) : RegexNode(parent) {

    override fun buildIteration(forger: Forger, builder: StringBuilder) {
        builder.append(forger.aWhitespaceChar())
    }

    override fun describe(builder: StringBuilder) {
        builder.append("\\s")
    }

}