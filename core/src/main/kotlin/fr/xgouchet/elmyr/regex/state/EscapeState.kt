package fr.xgouchet.elmyr.regex.state

import fr.xgouchet.elmyr.regex.node.Node
import fr.xgouchet.elmyr.regex.node.ParentNode
import fr.xgouchet.elmyr.regex.node.PredefinedCharacterClassNode
import fr.xgouchet.elmyr.regex.node.RawCharNode
import java.lang.IllegalStateException

internal class EscapeState(
    private val ongoingNode: ParentNode,
    private val previousState: State
) : State {

    // region State

    override fun handleChar(c: Char): State {
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

        return previousState
    }

    override fun getRoot(): Node {
        throw IllegalStateException("Unexpected end of expression after escape character")
    }

    // endregion
}