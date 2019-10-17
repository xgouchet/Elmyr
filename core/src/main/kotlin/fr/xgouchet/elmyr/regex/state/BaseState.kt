package fr.xgouchet.elmyr.regex.state

import fr.xgouchet.elmyr.regex.node.AlternationNode
import fr.xgouchet.elmyr.regex.node.DotMetacharacterNode
import fr.xgouchet.elmyr.regex.node.GroupNode
import fr.xgouchet.elmyr.regex.node.Node
import fr.xgouchet.elmyr.regex.node.ParentNode
import fr.xgouchet.elmyr.regex.node.RawCharNode
import fr.xgouchet.elmyr.regex.node.SequenceNode
import fr.xgouchet.elmyr.regex.quantifier.Quantifier

internal class BaseState(
    private val rootNode: ParentNode?,
    private val ongoingNode: ParentNode,
    private val previousState: State? = null
) : State {

    // region State

    override fun handleEndOfRegex(): Node {
        check(previousState == null) { "Illegal state, retrieving root node from non root state" }
        checkNotNull(rootNode) { "Illegal state, retrieving root node from non root state" }
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

            // Repetition class
            '{' -> newState = RepetitionState(ongoingNode, this)

            // Alternation
            '|' -> newState = handleAlternation()

            // Group
            '(' -> {
                val group = GroupNode(ongoingNode)
                ongoingNode.add(group)
                val sequence = SequenceNode(group)
                group.add(sequence)
                newState = BaseState(null, sequence, this)
            }
            ')' -> {
                checkNotNull(previousState) { "Illegal state when parsing regex" }
                newState = previousState
            }

            else -> ongoingNode.add(RawCharNode(c, ongoingNode))
        }

        return newState
    }

    // endregion

    // region Internal

    private fun handleAlternation(): State {
        val parentNode = ongoingNode.parentNode

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

        return BaseState(newRootNode, next, previousState)
    }

    // endregion
}