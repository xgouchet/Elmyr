package fr.xgouchet.elmyr.regex

/**
 * A RegexNode representing a capture group
 *
 * @author Xavier F. Gouchet
 */
class RegexGroupNode(parent: RegexParentNode? = null)
    : RegexParentNode(parent) {

    override fun describe(builder: StringBuilder) {
        builder.append(("("))
        super.describe(builder)
        builder.append((")"))
    }
}