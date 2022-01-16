package fr.xgouchet.elmyr.regex.state

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.regex.node.BaseParentNode
import fr.xgouchet.elmyr.regex.node.CharacterClassNode
import fr.xgouchet.elmyr.regex.node.CharacterRangeNode
import fr.xgouchet.elmyr.regex.node.Node
import fr.xgouchet.elmyr.regex.node.RawCharNode

internal class CharacterClassRangeState(
    private val classNode: CharacterClassNode,
    private val previousState: State
) : State {

    // region State

    override fun handleChar(c: Char): State {
        return when (c) {
            ']' -> {
                classNode.add(RawCharNode('-', "-"))
                previousState.handleChar(c)
            }
            '\\' -> EscapeState(
                object : BaseParentNode() {
                    override fun add(node: Node) {
                        val previous = classNode.removeLast() as? RawCharNode
                        checkNotNull(previous) { "Unexpected state in character class" }
                        check(node is RawCharNode) { "Unexpected state in character class" }
                        check(previous.rawChar < node.rawChar) {
                            "Illegal character range /${previous.escapedChar}-${node.escapedChar}/"
                        }
                        classNode.add(CharacterRangeNode(previous, node))
                    }

                    override fun build(forge: Forge, builder: StringBuilder) {
                        throw UnsupportedOperationException("Anonymous class can't build a regex")
                    }

                    override fun toRegex(): String {
                        throw UnsupportedOperationException("Anonymous class can't build a regex")
                    }
                }, previousState, false
            )
            else -> {
                val previous = classNode.removeLast()
                check(previous is RawCharNode) { "Unexpected state in character class" }
                check(previous.rawChar < c) { "Illegal character range /${previous.rawChar}-$c/" }
                classNode.add(CharacterRangeNode(previous, RawCharNode(c, "$c")))

                previousState
            }
        }
    }

    override fun handleEndOfRegex() {
        throw IllegalStateException("Unexpected end of expression in character class")
    }

    // endregion
}
