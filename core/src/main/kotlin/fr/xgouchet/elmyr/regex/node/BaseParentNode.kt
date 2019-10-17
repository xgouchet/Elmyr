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

    // endregion

    // region Node

    override fun check() {
        children.forEach { it.check() }
    }

    // endregion
}