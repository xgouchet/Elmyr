package fr.xgouchet.elmyr.regex.state

import fr.xgouchet.elmyr.regex.node.*
import fr.xgouchet.elmyr.regex.quantifier.Quantifier

internal class BaseState<T>(
    private val ongoingNode: T,
    private val previousState: State? = null
) : State
        where T : ParentNode,
              T : MovingChildNode,
              T : QuantifiableNode {

    // region State

    override fun handleEndOfRegex() {
        check(previousState == null) { "Illegal state, retrieving root node from non root state" }
    }

    override fun handleChar(c: Char): State {
        var newState: State = this

        when (c) {
            // Quantifiers
            '?' -> ongoingNode.handleQuantifier(Quantifier.MAYBE_ONE)
            '+' -> ongoingNode.handleQuantifier(Quantifier.ONE_OR_MORE)
            '*' -> ongoingNode.handleQuantifier(Quantifier.ZERO_OR_MORE)

            // Dot metacharacter
            '.' -> ongoingNode.add(DotMetacharacterNode())

            // Escape Sequence
            '\\' -> newState = EscapeState(ongoingNode, this, true)

            // Character class
            '[' -> newState = CharacterClassState(ongoingNode, this)

            // Repetition class
            '{' -> {
                newState = RepetitionState(ongoingNode, this)
            }

            // Alternation
            '|' -> newState = handleAlternation()

            // Group
            '(' -> {
                val group = GroupNode(ongoingNode)
                ongoingNode.add(group)
                val sequence = SequenceNode(group)
                group.add(sequence)
                newState = BaseState(sequence, this)
            }
            ')' -> {
                checkNotNull(previousState) { "Illegal state when parsing regex" }
                newState = previousState
            }

            else -> ongoingNode.add(RawCharNode(c))
        }

        return newState
    }

    // endregion

    // region Internal

    private fun handleAlternation(): State {

        println("Accessing parentNode for ${ongoingNode.javaClass.simpleName}")
        val parentNode = ongoingNode.getParent()

        // create alternation
        val alternation = AlternationNode()
        parentNode.remove(ongoingNode)
        parentNode.add(alternation)

        // update ongoing node
        alternation.add(ongoingNode)
        println("Updating parentNode for ${ongoingNode.javaClass.simpleName}")
        ongoingNode.updateParent(alternation)

        // create new sequence
        val next = SequenceNode(alternation)
        alternation.add(next)

        return BaseState(next, previousState)
    }

    // endregion
}