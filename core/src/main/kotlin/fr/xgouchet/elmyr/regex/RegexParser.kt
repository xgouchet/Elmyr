package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.regex.node.AlternationNode
import fr.xgouchet.elmyr.regex.node.CharacterClassNode
import fr.xgouchet.elmyr.regex.node.DotMetacharacterNode
import fr.xgouchet.elmyr.regex.node.ParentNode
import fr.xgouchet.elmyr.regex.node.PredefinedCharacterClassNode
import fr.xgouchet.elmyr.regex.node.Quantifier
import fr.xgouchet.elmyr.regex.node.RawCharNode
import fr.xgouchet.elmyr.regex.node.SequenceNode

internal class RegexParser {

    // TODO Add some memoization to not parse the same regex twice

    fun getFactory(regex: String): ForgeryFactory<String> {
        var rootNode: ParentNode = SequenceNode()
        var ongoingNode: ParentNode = rootNode
        var escapeNext = false

        for (c in regex) {
            if (escapeNext) {
                // previous character was a backslash -> escape the next char
                handleEscapedCharacter(ongoingNode, c)
                escapeNext = false
            } else if (c == '\\') {
                escapeNext = true
            } else {
                handleCharacter(rootNode, ongoingNode, c).let {
                    rootNode = it.first
                    ongoingNode = it.second
                }
            }
        }

        rootNode.verify()
        return RegexStringFactory(rootNode)
    }

    // region Internal

    private fun handleCharacter(
        rootNode: ParentNode,
        ongoingNode: ParentNode,
        c: Char
    ): Pair<ParentNode, ParentNode> {
        if (ongoingNode.handle(c)) {
            return rootNode to ongoingNode
        } else {

            return handleSpecialCharacter(rootNode, ongoingNode, c)
        }
    }

    @Suppress("ComplexMethod")
    private fun handleEscapedCharacter(ongoingNode: ParentNode, c: Char) {
        when (c) {
            // escapable characters
            '[', ']', '(', ')', '{', '}', '<', '>', '?', '+', '*',
            '-', '=', '!', '.', '|', '^', '$', '\\' -> ongoingNode.add(RawCharNode(c, ongoingNode))

            // Whitespace
            'n' -> ongoingNode.add(RawCharNode('\n', ongoingNode))
            't' -> ongoingNode.add(RawCharNode('\t', ongoingNode))
            'r' -> ongoingNode.add(RawCharNode('\r', ongoingNode))
            'f' -> ongoingNode.add(RawCharNode('\u000C', ongoingNode))
            'a' -> ongoingNode.add(RawCharNode('\u0007', ongoingNode))
            'e' -> ongoingNode.add(RawCharNode('\u001B', ongoingNode))

            // Predefined character classes
            'd' -> ongoingNode.add(PredefinedCharacterClassNode.digit(ongoingNode))
            'D' -> ongoingNode.add(PredefinedCharacterClassNode.notDigit(ongoingNode))
            'w' -> ongoingNode.add(PredefinedCharacterClassNode.word(ongoingNode))
            'W' -> ongoingNode.add(PredefinedCharacterClassNode.notWord(ongoingNode))
            's' -> ongoingNode.add(PredefinedCharacterClassNode.whitespace(ongoingNode))
            'S' -> ongoingNode.add(PredefinedCharacterClassNode.notWhitespace(ongoingNode))

            else -> throw IllegalStateException("Illegal/unsupported escape sequence /\\$c/")
        }
    }

    @Suppress("ComplexMethod")
    private fun handleSpecialCharacter(rootNode: ParentNode, ongoingNode: ParentNode, c: Char): Pair<ParentNode, ParentNode> {
        var newRootNode = rootNode
        var newOngoingNode = ongoingNode
        when (c) {
            // Dot metacharacter
            '.' -> ongoingNode.add(DotMetacharacterNode(ongoingNode))

            // Quantifiers
            '?' -> ongoingNode.handleQuantifier(Quantifier.MAYBE_ONE)
            '+' -> ongoingNode.handleQuantifier(Quantifier.ONE_OR_MORE)
            '*' -> ongoingNode.handleQuantifier(Quantifier.ZERO_OR_MORE)

            // Alternation
            '|' -> handleAlternation(rootNode, ongoingNode).let {
                newRootNode = it.first
                newOngoingNode = it.second
            }

            // Character class
            '[' -> {
                val set = CharacterClassNode(ongoingNode)
                ongoingNode.add(set)
                newOngoingNode = set
            }
            ']' -> {
                if (ongoingNode is CharacterClassNode) {
                    ongoingNode.close()
                    val parentNode = ongoingNode.parentNode
                    checkNotNull(parentNode)
                    newOngoingNode = parentNode
                } else {
                    ongoingNode.add(RawCharNode(c, ongoingNode))
                }
            }

            else -> ongoingNode.add(RawCharNode(c, ongoingNode))
        }

        return newRootNode to newOngoingNode
    }

    private fun handleAlternation(
        rootNode: ParentNode,
        ongoingNode: ParentNode
    ): Pair<ParentNode, ParentNode> {
        val parentNode = ongoingNode.parentNode
        return if (parentNode is AlternationNode) {
            val next = SequenceNode(parentNode)
            parentNode.add(next)
            rootNode to next
        } else if (parentNode is SequenceNode?) {
            // create alternation
            val alternation = AlternationNode(parentNode)
            parentNode?.remove(ongoingNode)
            parentNode?.add(alternation)

            // update ongoing node
            alternation.add(ongoingNode)
            ongoingNode.parentNode = alternation

            // create new sequence
            val next = SequenceNode(alternation)
            alternation.add(next)

            // update root ?
            val newRootNode = if (ongoingNode == rootNode || parentNode == rootNode) {
                alternation
            } else {
                rootNode
            }

            newRootNode to next
        } else {
            throw IllegalStateException("Whateer")
        }
    }

    // endregion
}