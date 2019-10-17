package fr.xgouchet.elmyr.regex.state

import fr.xgouchet.elmyr.regex.node.Node
import fr.xgouchet.elmyr.regex.node.ParentNode
import fr.xgouchet.elmyr.regex.quantifier.Quantifier
import fr.xgouchet.elmyr.regex.quantifier.QuantifierAtLeastN
import fr.xgouchet.elmyr.regex.quantifier.QuantifierExactlyN
import fr.xgouchet.elmyr.regex.quantifier.QuantifierRange

internal class RepetitionState(
    private val ongoingNode: ParentNode,
    private val previousState: State
) : State {

    private var atLeastOneDigit = false
    private var beforeComma = true

    private var from = 0
    private var to = 0

    // region State

    override fun handleChar(c: Char): State {
        var newState: State = this
        when (c) {
            '}' -> {
                ongoingNode.handleQuantifier(getQuantifier())
                newState = previousState
            }
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                atLeastOneDigit = true
                val digit = (c - '0')
                if (beforeComma) {
                    from = (from * BASE_10) + digit
                } else {
                    to = (to * BASE_10) + digit
                }
            }
            ',' -> {
                check(atLeastOneDigit) { "Invalid repetition range" }
                beforeComma = false
            }
            else -> throw IllegalStateException("Unexpected character in repetition range")
        }

        return newState
    }

    override fun getRoot(): Node {
        throw IllegalStateException("Unexpected end of expression in repetition range")
    }

    // endregion

    // region Internal

    private fun getQuantifier(): Quantifier {
        check(atLeastOneDigit) { "Invalid repetition range" }
        return if (beforeComma) {
            QuantifierExactlyN(from)
        } else if (to == 0) {
            QuantifierAtLeastN(from)
        } else {
            check(from <= to) { "Invalid repetition range /{$from,$to}" }
            QuantifierRange(from, to)
        }
    }

    // endregion

    companion object {
        private const val BASE_10 = 10
    }
}
