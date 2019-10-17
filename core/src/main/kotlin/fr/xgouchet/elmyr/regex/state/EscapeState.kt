package fr.xgouchet.elmyr.regex.state

import fr.xgouchet.elmyr.regex.node.BackReferenceNode
import fr.xgouchet.elmyr.regex.node.Node
import fr.xgouchet.elmyr.regex.node.ParentNode
import fr.xgouchet.elmyr.regex.node.PredefinedCharacterClassNode
import fr.xgouchet.elmyr.regex.node.RawCharNode

internal class EscapeState(
    private val ongoingNode: ParentNode,
    private val previousState: State
) : State {

    var readingBackReference = false
    var backReference = 0

    // region State

    override fun handleChar(c: Char): State {
        return if (readingBackReference) {
            handleBackReferenceChars(c)
        } else {
            handleStandardEscapeChars(c)
        }
    }

    override fun handleEndOfRegex(): Node {
        if (readingBackReference) {
            ongoingNode.add(BackReferenceNode(backReference, ongoingNode))
            return previousState.handleEndOfRegex()
        } else {
            throw IllegalStateException("Unexpected end of expression after escape character")
        }
    }

    // endregion

    // region Internal

    private fun handleStandardEscapeChars(c: Char): State {
        var newState = previousState

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

            '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                readingBackReference = true
                val digit = (c - '0')
                backReference = (backReference * BASE_10) + digit
                newState = this
            }

            else -> throw IllegalStateException("Illegal/unsupported escape sequence /\\$c/")
        }
        return newState
    }

    private fun handleBackReferenceChars(c: Char): State {
        var newState: State = this
        when (c) {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                val digit = (c - '0')
                backReference = (backReference * BASE_10) + digit
            }
            else -> {
                ongoingNode.add(BackReferenceNode(backReference, ongoingNode))
                newState = previousState.handleChar(c)
            }
        }

        return newState
    }

    // endregion

    companion object {
        private const val BASE_10 = 10
    }
}