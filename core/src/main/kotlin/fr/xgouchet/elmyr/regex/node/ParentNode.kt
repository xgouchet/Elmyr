package fr.xgouchet.elmyr.regex.node

internal interface ParentNode :
        Node {

    fun add(node: Node)

    fun remove(node: Node)

    fun isEmpty(): Boolean

    fun flattenHierarchy(): List<Node>
}