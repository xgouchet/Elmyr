package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

/**
 * Describe a back reference to a previously captured group.
 * e.g.: ```/(a*)bc\1/```
 */
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

    override fun toRegex(): String {
        return "\\$groupReference"
    }

    // endregion

    // region Object

    override fun toString(): String {
        return "BackReferenceNode(ref:$groupReference)"
    }

    // endregion
}
