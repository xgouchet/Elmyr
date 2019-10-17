package fr.xgouchet.elmyr.regex.state

import fr.xgouchet.elmyr.regex.node.AlternationNode
import fr.xgouchet.elmyr.regex.node.DotMetacharacterNode
import fr.xgouchet.elmyr.regex.node.Node
import fr.xgouchet.elmyr.regex.node.ParentNode
import fr.xgouchet.elmyr.regex.node.Quantifier
import fr.xgouchet.elmyr.regex.node.RawCharNode
import fr.xgouchet.elmyr.regex.node.SequenceNode

internal class BaseState(
    private val rootNode: ParentNode,
    private val ongoingNode: ParentNode
) : State {

    // region State

    override fun getRoot(): Node {
        return rootNode
    }

    override fun handleChar(c: Char): State {
        var newState: State = this

        when (c) {
            // Quantifiers
            '?' -> ongoingNode.handleQuantifier(Quantifier.MAYBE_ONE)
            '+' -> ongoingNode.handleQuantifier(Quantifier.ONE_OR_MORE)
            '*' -> ongoingNode.handleQuantifier(Quantifier.ZERO_OR_MORE)

            // Dot metacharacter
            '.' -> ongoingNode.add(DotMetacharacterNode(ongoingNode))

            // Escape Sequence
            '\\' -> newState = EscapeState(ongoingNode, this)

            // Character class
            '[' -> newState = CharacterClassState(ongoingNode, this)

            // Alternation
            '|' -> newState = handleAlternation()

            else -> ongoingNode.add(RawCharNode(c, ongoingNode))
        }

        return newState
    }

    // endregion

    private fun handleAlternation(): State {
        val parentNode = ongoingNode.parentNode
        return if (parentNode is AlternationNode) {
            val next = SequenceNode(parentNode)
            parentNode.add(next)
            BaseState(rootNode, next)
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

            BaseState(newRootNode, next)
        } else {
            throw IllegalStateException("Whatever")
        }
    }
}