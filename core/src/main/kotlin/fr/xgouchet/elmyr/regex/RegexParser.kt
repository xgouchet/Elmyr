package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.regex.node.SequenceNode
import fr.xgouchet.elmyr.regex.state.BaseState
import fr.xgouchet.elmyr.regex.state.State

internal class RegexParser {

    // TODO Add some memoization to not parse the same regex twice

    // region RegexParser

    fun getFactory(regex: String): ForgeryFactory<String> {

        var state: State = SequenceNode().let {
            BaseState(it, it)
        }

        for (c in regex) {
            state = state.handleChar(c)
        }

        val rootNode = state.handleEndOfRegex()
        rootNode.check()
        return RegexStringFactory(rootNode)
    }

    // endregion
}