package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * Represents a choice
 * @author Xavier F. Gouchet
 */
internal class RegexChoiceNode(parent: RegexParentNode? = null)
    : RegexParentNode(parent) {

    private var ongoingRange: Boolean = false

    override fun handle(c: Char): Boolean {
        return when (c) {
            '*', '.', '+', '?' -> {
                add(RawChar(c, this))
                true
            }
            '-' -> {
                ongoingRange = true
                true
            }
            else -> false
        }
    }

    override fun add(b: RegexNode) {
        if (!ongoingRange) {
            super.add(b)
        } else {
            val previous = children.last()
            val range = buildRange(previous, b)
            if (range != null) {
                children.remove(previous)
                children.add(range)
            }
            ongoingRange = false
        }
    }

    override fun describe(builder: StringBuilder) {
        builder.append(("["))
        super.describe(builder)
        builder.append(("]"))
    }

    override fun buildIteration(forger: Forger, builder: StringBuilder) {
        val node = forger.anElementFrom(children)
        node.buildIteration(forger, builder)
    }

    // TODO cleanup this method !
    @Suppress("ReturnCount")
    private fun buildRange(from: RegexNode, to: RegexNode): RegexNode? {
        if (from !is RawChar) return null
        if (to !is RawChar) return null

        val fromChar = from.rawChar
        val toChar = to.rawChar

        if (fromChar >= toChar) return null

        if (Forger.ALPHA_LOWER.contains(fromChar) and !Forger.ALPHA_LOWER.contains(toChar)) return null
        if (Forger.ALPHA_UPPER.contains(fromChar) and !Forger.ALPHA_UPPER.contains(toChar)) return null
        if (Forger.DIGIT.contains(fromChar) and !Forger.DIGIT.contains(toChar)) return null

        return RegexCharRangeNode(fromChar, toChar, this)
    }

}
