package fr.xgouchet.elmyr.regex.state

import fr.xgouchet.elmyr.regex.node.BackReferenceNode
import fr.xgouchet.elmyr.regex.node.ParentNode
import fr.xgouchet.elmyr.regex.node.PredefinedCharacterClassNode
import fr.xgouchet.elmyr.regex.node.RawCharNode

internal class EscapeState(
    private val ongoingNode: ParentNode,
    private val previousState: State,
    private val allowBackReference: Boolean
) : State {

    private var readingBackReference = false
    private var backReference = 0

    // region State

    override fun handleChar(c: Char): State {
        return if (readingBackReference) {
            handleBackReferenceChars(c)
        } else {
            handleStandardEscapeChars(c)
        }
    }

    override fun handleEndOfRegex() {
        if (readingBackReference) {
            ongoingNode.add(BackReferenceNode(backReference, ongoingNode))
            previousState.handleEndOfRegex()
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
            '-', '=', '!', '.', '|', '^', '$', '\\' -> ongoingNode.add(RawCharNode(c, "\\$c"))

            // Whitespace
            'n' -> ongoingNode.add(RawCharNode('\n', "\\$c"))
            't' -> ongoingNode.add(RawCharNode('\t', "\\$c"))
            'r' -> ongoingNode.add(RawCharNode('\r', "\\$c"))
            'f' -> ongoingNode.add(RawCharNode('\u000C', "\\$c"))
            'a' -> ongoingNode.add(RawCharNode('\u0007', "\\$c"))
            'e' -> ongoingNode.add(RawCharNode('\u001B', "\\$c"))

            // Predefined character classes
            'd' -> ongoingNode.add(PredefinedCharacterClassNode.digit())
            'D' -> ongoingNode.add(PredefinedCharacterClassNode.notDigit())
            'w' -> ongoingNode.add(PredefinedCharacterClassNode.word())
            'W' -> ongoingNode.add(PredefinedCharacterClassNode.notWord())
            's' -> ongoingNode.add(PredefinedCharacterClassNode.whitespace())
            'S' -> ongoingNode.add(PredefinedCharacterClassNode.notWhitespace())

            '1', '2', '3', '4', '5', '6', '7', '8', '9' -> if (allowBackReference) {
                readingBackReference = true
                val digit = (c - '0')
                backReference = (backReference * BASE_10) + digit
                newState = this
            } else {
                throw IllegalStateException("Illegal/unsupported escape sequence /\\$c/")
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
