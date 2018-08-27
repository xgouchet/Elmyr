package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * A Basic Regex Node, able to include child nodes
 *
 * @author Xavier F. Gouchet
 */
open class RegexParentNode(parent: RegexParentNode? = null) : RegexNode(parent) {

    internal val children: MutableList<RegexNode> = ArrayList()

    open fun add(b: RegexNode) {
        children.add(b)
    }

    fun remove(node: RegexNode) {
        children.remove(node)
    }

    fun updateLastElementQuantfier(quantifier: Quantifier) {
        children.last().quantifier = quantifier
    }

    override fun buildIteration(forger: Forger, builder: StringBuilder) {
        for (sub in children) {
            sub.build(forger, builder)
        }
    }

    open fun handle(c: Char): Boolean = false

    override fun describe(builder: StringBuilder) {
        for (p in children) {
            builder.append(p)
        }
    }

    fun popLastNode(): RegexNode {
        val lastNode = children.last()
        children.remove(lastNode)
        return lastNode
    }
}
