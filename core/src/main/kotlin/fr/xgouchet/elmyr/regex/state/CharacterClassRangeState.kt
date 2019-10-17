package fr.xgouchet.elmyr.regex.state

import fr.xgouchet.elmyr.regex.node.CharacterClassNode
import fr.xgouchet.elmyr.regex.node.CharacterRangeNode
import fr.xgouchet.elmyr.regex.node.Node
import fr.xgouchet.elmyr.regex.node.RawCharNode

internal class CharacterClassRangeState(
    private val classNode: CharacterClassNode,
    private val previousState: State
) : State {

    override fun handleChar(c: Char): State {
        return when (c) {
            ']' -> {
                classNode.add(RawCharNode('-', classNode))
                previousState.handleChar(c)
            }
            else -> {
                val previous = classNode.removeLast()
                check(previous is RawCharNode) { "Unexpected state in character class" }
                classNode.add(CharacterRangeNode(previous.rawChar, c, classNode))

                previousState
            }
        }
    }

    override fun getRoot(): Node {
        throw IllegalStateException("Unexpected end of expression in character class")
    }
}