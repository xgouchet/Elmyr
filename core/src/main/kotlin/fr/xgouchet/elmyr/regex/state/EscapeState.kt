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
    private var readingOctal = false
    private var readingHexadecimal = false
    private var readingUnicode = false
    private var backReference = 0
    private var hexadecimalValue = 0
    private var octalValue = 0
    private var escapedStr = ""

    // region State

    override fun handleChar(c: Char): State {
        return if (readingBackReference) {
            handleBackReferenceChars(c)
        } else if (readingUnicode || readingHexadecimal) {
            handleHexadecimalChars(c)
        } else if (readingOctal) {
            handleOctalChars(c)
        } else {
            handleStandardEscapeChars(c)
        }
    }

    override fun handleEndOfRegex() {
        if (readingBackReference) {
            ongoingNode.add(BackReferenceNode(backReference, ongoingNode))
            previousState.handleEndOfRegex()
        } else if (readingUnicode) {
            runIfNonEmptyEscapedString("unicode character") {
                val escapedChar = "\\u${hexadecimalValue.toString(BASE_16).padStart(4, '0')}"
                check(hexadecimalValue in CHAR_MIN..CHAR_MAX) { "Invalid unicode value: $escapedChar" }
                ongoingNode.add(RawCharNode(hexadecimalValue.toChar(), escapedChar))
                previousState.handleEndOfRegex()
            }
        } else if (readingHexadecimal) {
            runIfNonEmptyEscapedString("hexadecimal number") {
                val escapedChar = "\\x${hexadecimalValue.toString(BASE_16).padStart(2, '0')}"
                check(hexadecimalValue in CHAR_MIN..CHAR_MAX) { "Invalid hexadecimal value: $escapedChar" }
                ongoingNode.add(RawCharNode(hexadecimalValue.toChar(), escapedChar))
                previousState.handleEndOfRegex()
            }
        } else if (readingOctal) {
            runIfNonEmptyEscapedString("octal number") {
                val escapedChar = "\\0${octalValue.toString(BASE_8)}"
                check(octalValue in CHAR_MIN..CHAR_MAX) { "Invalid octal value: $escapedChar" }
                ongoingNode.add(RawCharNode(octalValue.toChar(), escapedChar))
                previousState.handleEndOfRegex()
            }
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

            // Escaped sequence
            '0' -> {
                readingOctal = true
                newState = this
            }
            'x' -> {
                readingHexadecimal = true
                newState = this
            }
            'u' -> {
                readingUnicode = true
                newState = this
            }

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
                escapedStr += c
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

    private fun handleHexadecimalChars(c: Char): State {
        var newState: State = this
        when (c) {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                escapedStr += c
                val digit = (c - '0')
                hexadecimalValue = (hexadecimalValue * BASE_16) + digit
            }
            'a', 'b', 'c', 'd', 'e', 'f' -> {
                escapedStr += c
                val digit = (c - 'a') + 10
                hexadecimalValue = (hexadecimalValue * BASE_16) + digit
            }
            'A', 'B', 'C', 'D', 'E', 'F' -> {
                escapedStr += c
                val digit = (c - 'A') + 10
                hexadecimalValue = (hexadecimalValue * BASE_16) + digit
            }
            else -> if (readingUnicode) {
                newState = runIfNonEmptyEscapedString("unicode character") {
                    val escapedChar = "\\u${hexadecimalValue.toString(BASE_16).padStart(4, '0')}"
                    check(hexadecimalValue in CHAR_MIN..CHAR_MAX) { "Invalid unicode value: $escapedChar" }
                    ongoingNode.add(RawCharNode(hexadecimalValue.toChar(), escapedChar))
                    previousState.handleChar(c)
                }
            } else if (readingHexadecimal) {
                newState = runIfNonEmptyEscapedString("hexadecimal number") {
                    val escapedChar = "\\x${hexadecimalValue.toString(BASE_16).padStart(2, '0')}"
                    check(hexadecimalValue in CHAR_MIN..CHAR_MAX) { "Invalid hexadecimal value: $escapedChar" }
                    ongoingNode.add(RawCharNode(hexadecimalValue.toChar(), escapedChar))
                    previousState.handleChar(c)
                }
            }
        }

        return newState
    }

    private fun handleOctalChars(c: Char): State {
        var newState: State = this
        when (c) {
            '0', '1', '2', '3', '4', '5', '6', '7' -> {
                escapedStr += c
                val digit = (c - '0')
                octalValue = (octalValue * BASE_8) + digit
            }
            else -> {
                newState = runIfNonEmptyEscapedString("octal number") {
                    val escapedChar = "\\0${octalValue.toString(BASE_8)}"
                    check(octalValue in CHAR_MIN..CHAR_MAX) { "Invalid octal value: $escapedChar" }
                    ongoingNode.add(RawCharNode(octalValue.toChar(), escapedChar))
                    previousState.handleChar(c)
                }
            }
        }

        return newState
    }

    private fun <T> runIfNonEmptyEscapedString(
        type: String,
        block: () -> T
    ): T {
        if (escapedStr.isEmpty()) {
            throw IllegalStateException("Invalid format: expecting $type")
        } else {
            return block()
        }
    }

    // endregion

    companion object {
        private const val BASE_8 = 8
        private const val BASE_10 = 10
        private const val BASE_16 = 16

        private const val CHAR_MIN = Char.MIN_VALUE.toInt()
        private const val CHAR_MAX = Char.MAX_VALUE.toInt()
    }
}
