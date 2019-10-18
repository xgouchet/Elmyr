package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class GroupNode(
    private val parentNode: ParentNode
) : BaseParentNode(), ChildNode {

    internal var referenceValue: String = ""
        private set

    // region ChildNode

    override fun getParent(): ParentNode = parentNode

    // endregion

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        val localBuilder = StringBuilder()
        children.first().build(forge, localBuilder)
        referenceValue = localBuilder.toString()
        builder.append(localBuilder.toString())
    }

    // endregion
}