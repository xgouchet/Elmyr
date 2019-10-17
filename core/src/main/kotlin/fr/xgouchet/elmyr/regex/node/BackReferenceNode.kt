package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class BackReferenceNode(
    val groupReference: Int,
    override var parentNode: ParentNode?
) : Node {

    var referencedGroup: GroupNode? = null

    override fun build(forge: Forge, builder: StringBuilder) {
        referencedGroup?.let {
            builder.append(it.referenceValue)
        }
    }

    override fun check() {
        val root = getRoot()
        checkNotNull(root) { "Illegal state BackReference not within a hierarchy" }
        referencedGroup = root.findGroup(groupReference)
    }
}