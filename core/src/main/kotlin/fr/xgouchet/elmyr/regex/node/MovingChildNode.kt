package fr.xgouchet.elmyr.regex.node

internal interface MovingChildNode : ChildNode {

    fun updateParent(parentNode: ParentNode)
}