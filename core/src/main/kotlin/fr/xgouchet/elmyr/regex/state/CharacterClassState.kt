package fr.xgouchet.elmyr.regex.state

import fr.xgouchet.elmyr.regex.node.CharacterClassNode
import fr.xgouchet.elmyr.regex.node.ParentNode
import fr.xgouchet.elmyr.regex.node.RawCharNode

internal class CharacterClassState(
    private val ongoingNode: ParentNode,
    private val previousState: State,
    private val isNegation: Boolean = false
) : State {

    private val classNode = CharacterClassNode(isNegation)

    // region State

    override fun handleChar(c: Char): State {
        var newState: State = this
        when (c) {
            // Escape Sequence
            '\\' -> newState = EscapeState(classNode, this, false)

            // Closing Bracket
            ']' -> {
                classNode.close()
                ongoingNode.add(classNode)
                newState = previousState
            }

            // Inner Class
            '[' -> newState = CharacterClassState(ongoingNode, this)

            // Character range
            '-' -> if (classNode.isEmpty()) {
                classNode.add(RawCharNode(c))
            } else {
                newState = CharacterClassRangeState(classNode, this)
            }

            // Negation
            '^' -> if (classNode.isEmpty()) {
                newState = CharacterClassState(ongoingNode, previousState, true)
            } else {
                classNode.add(RawCharNode(c))
            }

            else -> classNode.add(RawCharNode(c))
        }

        return newState
    }

    override fun handleEndOfRegex() {
        throw IllegalStateException("Unexpected end of expression in character class")
    }

    // endregion
}