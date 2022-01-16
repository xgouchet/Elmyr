package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.regex.node.BackReferenceNode
import fr.xgouchet.elmyr.regex.node.GroupNode
import fr.xgouchet.elmyr.regex.node.RootNode
import fr.xgouchet.elmyr.regex.node.SequenceNode
import fr.xgouchet.elmyr.regex.state.BaseState
import fr.xgouchet.elmyr.regex.state.State

internal class RegexParser {

    val cache = LRUCache<String, ForgeryFactory<String>>(REGEX_CACHE_SIZE) { regex ->
        generateFactory(regex)
    }

    // region RegexParser

    fun getFactory(regex: String): ForgeryFactory<String> {
        return cache.get(regex)
    }

    // endregion

    // region Internal

    private fun generateFactory(regex: String): ForgeryFactory<String> {

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

    companion object {
        const val REGEX_CACHE_SIZE = 64
    }
}
