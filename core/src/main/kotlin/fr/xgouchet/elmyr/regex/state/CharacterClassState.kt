package fr.xgouchet.elmyr.regex.state

import fr.xgouchet.elmyr.regex.node.CharacterClassNode
import fr.xgouchet.elmyr.regex.node.Node
import fr.xgouchet.elmyr.regex.node.ParentNode
import fr.xgouchet.elmyr.regex.node.RawCharNode

internal class CharacterClassState(
    private val ongoingNode: ParentNode,
    private val previousState: State
) : State {

    val classNode = CharacterClassNode(ongoingNode)

    // region State

    override fun handleChar(c: Char): State {
        var newState: State = this
        when (c) {
            // Escape Sequence
            '\\' -> newState = EscapeState(classNode, this)

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
                classNode.add(RawCharNode(c, classNode))
            } else {
                newState = CharacterClassRangeState(classNode, this)
            }

            // Negation
            '^' -> if (classNode.isEmpty()) {
                classNode.isNegation = true
            } else {
                classNode.add(RawCharNode(c, classNode))
            }

            else -> classNode.add(RawCharNode(c, classNode))
        }

        return newState
    }

    override fun handleEndOfRegex(): Node {
        throw IllegalStateException("Unexpected end of expression in character class")
    }

    // endregion
}