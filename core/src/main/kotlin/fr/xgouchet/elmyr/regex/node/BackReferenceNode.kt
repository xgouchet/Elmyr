package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class BackReferenceNode(
    internal val groupReference: Int,
    private val parentNode: ParentNode
) : ChildNode {

    internal lateinit var referencedGroup: GroupNode

    // region ChildNode

    override fun getParent(): ParentNode = parentNode

    // endregion

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        builder.append(referencedGroup.referenceValue)
    }

    // endregion
}
