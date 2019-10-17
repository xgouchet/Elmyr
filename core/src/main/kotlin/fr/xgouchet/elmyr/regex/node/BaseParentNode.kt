package fr.xgouchet.elmyr.regex.node

internal abstract class BaseParentNode : ParentNode {

    protected val children: MutableList<Node> = mutableListOf()

    // region ParentNode

    override fun add(node: Node) {
        children.add(node)
    }

    override fun remove(node: Node) {
        children.remove(node)
    }

    override fun isEmpty(): Boolean {
        return children.isEmpty()
    }

    override fun flattenHierarchy(): List<Node> {
        val list = mutableListOf<Node>()
        list.add(this)
        children.forEach {
            if (it is ParentNode) {
                it.flattenHierarchy().forEach { node -> list.add(node) }
            } else {
                list.add(it)
            }
        }
        return list
    }

    override fun findGroup(groupReference: Int): GroupNode? {
        val nodes = flattenHierarchy()
        val groupNodes = nodes.filterIsInstance<GroupNode>()
        return groupNodes.getOrNull(groupReference - 1)
    }

    // endregion

    // region Node

    override fun check() {
        children.forEach { it.check() }
    }

    // endregion
}