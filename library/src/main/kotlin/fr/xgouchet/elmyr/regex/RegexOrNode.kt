package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * @author Xavier F. Gouchet
 */
class RegexOrNode(parent: RegexParentNode? = null) : RegexParentNode(parent) {

    override fun describe(builder: StringBuilder) {
        for ((index, child) in children.withIndex()) {
            if (index > 0) {
                builder.append('|')
            }
            child.describe(builder)
        }
    }

    override fun buildIteration(forger: Forger, builder: StringBuilder) {
        val child = forger.anElementFrom(children)
        child.buildIteration(forger, builder)
    }
}
