package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * Represents a non whitespace character
 *
 * @author Xavier F. Gouchet
 */
class RegexNonWhitespaceNode(parent: RegexParentNode)
    : RegexNode(parent) {

    override fun buildIteration(forger: Forger, builder: StringBuilder) {
        builder.append(forger.aNonWhitespaceChar())
    }

    override fun describe(builder: StringBuilder) {
        builder.append("\\S")
    }

}