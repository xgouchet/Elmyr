package fr.xgouchet.elmyr.regex.node

internal interface ChildNode : Node {

    fun getParent(): ParentNode

    fun isDescendantOf(node: ParentNode): Boolean {
        val parent = getParent()
        return if (this == node || parent == node) {
            true
        } else if (parent is ChildNode) {
            parent.isDescendantOf(node)
        } else {
            false
        }
    }
}