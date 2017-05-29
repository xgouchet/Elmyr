package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * @author Xavier F. Gouchet
 */
class RegexBuilder(regex: String) {

    internal var rootNode: RegexParentNode = RegexParentNode()
    internal var ongoingNode: RegexParentNode = rootNode
    internal var escapeNext = false

    init {
        parse(regex)
    }

    private fun parse(regex: String) {
        for (c in regex.toCharArray()) {
            if (escapeNext) {
                // previous character was a backslash -> escape the next char
                handleEscapedCharacter(c)
                escapeNext = false
            } else {
                handleCharacter(c)
            }
        }
    }

    private fun handleEscapedCharacter(c: Char) {
        when (c) {
        // character classes
            's' -> ongoingNode.add(RegexWhitespaceNode(ongoingNode))
            'S' -> ongoingNode.add(RegexNonWhitespaceNode(ongoingNode))
            'w' -> ongoingNode.add(RegexWordCharNode(ongoingNode))
            'd' -> ongoingNode.add(RegexDigitCharNode(ongoingNode))
            'W' -> ongoingNode.add(RegexNonWordCharNode(ongoingNode))
            'D' -> ongoingNode.add(RegexNonDigitCharNode(ongoingNode))

        // whitespaces
            'n' -> ongoingNode.add(RawChar('\n', ongoingNode))

        // literal escaped characters
            '\\', '|', '^', '-', '=', '$', '!', '?', '*', '+', '.',
            '{', '}', '(', ')', '[', ']', '<', '>' -> ongoingNode.add(RawChar(c, ongoingNode))

            else -> throw IllegalStateException("Can't escape ‘$c’")
        }
    }


    private fun handleCharacter(c: Char) {
        if (ongoingNode.handle(c)) return

        when (c) {
            '.' -> ongoingNode.add(RegexWildcardNode(ongoingNode))
            '?' -> ongoingNode.updateLastElementQuantfier(Quantifier.Companion.MAYBE_ONE)
            '*' -> ongoingNode.updateLastElementQuantfier(Quantifier.Companion.ZERO_OR_MORE)
            '+' -> ongoingNode.updateLastElementQuantfier(Quantifier.Companion.ONE_OR_MORE)
            '[' -> {
                val choice = RegexChoiceNode(ongoingNode)
                ongoingNode.add(choice)
                ongoingNode = choice
            }
            ']' -> {
                ongoingNode = ongoingNode.parent ?: throw IllegalStateException()
            }
            '(' -> {
                val group = RegexGroupNode(ongoingNode)
                ongoingNode.add(group)
                ongoingNode = group
            }
            ')' -> {
                ongoingNode = ongoingNode.parent ?: throw IllegalStateException()
            }
            '{' -> {
                val group = RegexRangeNode(ongoingNode)
                ongoingNode = group
            }
            '}' -> {
                val rangeNode = ongoingNode as? RegexRangeNode ?: throw IllegalStateException("Expecting to be reading a range")
                val rangeQuantifier = rangeNode.toQuantifier()
                ongoingNode = ongoingNode.parent ?: throw IllegalStateException()
                ongoingNode.updateLastElementQuantfier(rangeQuantifier)
            }
            '|' -> {
                if (ongoingNode.parent is RegexOrNode) {
                    val next = RegexParentNode(ongoingNode.parent)
                    ongoingNode.parent?.add(next)
                    ongoingNode = next
                } else {
                    // create or node
                    val orNode = RegexOrNode(ongoingNode.parent)

                    // link with parent
                    orNode.add(ongoingNode)
                    ongoingNode.parent?.let { it.add(orNode) }

                    // update root node
                    if (ongoingNode == rootNode) {
                        rootNode = orNode
                    } else if (ongoingNode.parent == rootNode) {
                        rootNode = orNode
                    }

                    // create new sequence
                    val next = RegexParentNode(orNode)
                    orNode.add(next)

                    // update ongoing node
                    ongoingNode.parent?.let { it.remove(ongoingNode) }
                    ongoingNode.parent = orNode
                    ongoingNode = next
                }
            }
            '\\' -> escapeNext = true
            else -> {
                ongoingNode.add(RawChar(c, ongoingNode))
            }
        }
    }

    fun buildString(forger: Forger): String {
        val builder = StringBuilder()
        rootNode.build(forger, builder)
        return builder.toString()
    }

}

