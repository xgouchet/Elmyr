package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * Represents a character set, eg [a-f]
 * @author Xavier F. Gouchet
 */
internal class RegexCharacterSet(parent: RegexParentNode? = null)
    : RegexParentNode(parent) {

    private var ongoingRange: Boolean = false
    private var isNegated: Boolean = false

    private var negatedRegex: String = ""

    override fun handle(c: Char): Boolean {
        return if (isNegated) {
            if (c != ']') {
                negatedRegex += c
                true
            } else {
                false
            }
        } else {
            when (c) {
                '^' -> {
                    if (children.isEmpty()) {
                        isNegated = true
                        true
                    } else {
                        false
                    }
                }
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
        if (isNegated) {
            val regex = Regex("[$negatedRegex]")
            var char: Char
            do {
                char = forger.aChar()
            } while (regex.matches("$char"))
            builder.append(char)
        } else {
            val node = forger.anElementFrom(children)
            node.buildIteration(forger, builder)
        }
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
