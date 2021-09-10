package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.regex.node.BackReferenceNode
import fr.xgouchet.elmyr.regex.node.GroupNode
import fr.xgouchet.elmyr.regex.node.RootNode
import fr.xgouchet.elmyr.regex.node.SequenceNode
import fr.xgouchet.elmyr.regex.state.BaseState
import fr.xgouchet.elmyr.regex.state.State
import java.lang.IllegalStateException

internal class RegexParser {

    // TODO #56 Add some memoization to not parse the same regex twice

    // region RegexParser

    fun getFactory(regex: String): ForgeryFactory<String> {

        val root = RootNode()
        val sequence = SequenceNode(root).also { root.add(it) }

        var state: State = BaseState(sequence)

        for (c in regex) {
            state = state.handleChar(c)
        }
        state.handleEndOfRegex()

        // validate back refs
        val hierarchy = root.flattenHierarchy()
        val groups = hierarchy.filterIsInstance<GroupNode>()
        hierarchy.forEach {
            if (it is BackReferenceNode) {
                val ref = it.groupReference
                if (ref > groups.size) {
                    throw IllegalStateException("Illegal back reference /\\$ref/, not enough groups")
                } else {
                    val groupNode = groups[ref - 1]
                    check(!it.isDescendantOf(groupNode)) { "Illegal recursive back reference /\\$ref/" }
                    it.referencedGroup = groupNode
                }
            }
        }

        return RegexStringFactory(root)
    }

    // endregion
}
