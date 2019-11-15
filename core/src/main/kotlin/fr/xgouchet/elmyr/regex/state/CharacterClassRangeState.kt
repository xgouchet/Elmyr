package fr.xgouchet.elmyr.regex.state

import fr.xgouchet.elmyr.regex.node.CharacterClassNode
import fr.xgouchet.elmyr.regex.node.CharacterRangeNode
import fr.xgouchet.elmyr.regex.node.RawCharNode

internal class CharacterClassRangeState(
    private val classNode: CharacterClassNode,
    private val previousState: State
) : State {

    // region State

    override fun handleChar(c: Char): State {
        return when (c) {
            ']' -> {
                classNode.add(RawCharNode('-'))
                previousState.handleChar(c)
            }
            else -> {
                val previous = classNode.removeLast()
                check(previous is RawCharNode) { "Unexpected state in character class" }
                check(previous.rawChar < c) { "Illegal character range /${previous.rawChar}-$c/" }
                classNode.add(CharacterRangeNode(previous.rawChar, c))

                previousState
            }
        }
    }

    override fun handleEndOfRegex() {
        throw IllegalStateException("Unexpected end of expression in character class")
    }

    // endregion
}
