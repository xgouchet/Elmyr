package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.regex.quantifier.Quantifier

internal open class SequenceNode(
    private var parentNode: ParentNode
) : BaseParentNode(), MovingChildNode, QuantifiableNode {

    // region QuantifiableNode

    override fun handleQuantifier(quantifier: Quantifier) {
        if (children.isNotEmpty()) {
            val lastElement = children.removeAt(children.lastIndex)
            val newElement = QuantifiedNode(
                    node = lastElement,
                    quantifier = quantifier
            )
            children.add(newElement)
        }
    }

    // endregion

    // region ChildNode

    override fun getParent(): ParentNode = parentNode

    // endregion

    // region MovingChildNode

    override fun updateParent(parentNode: ParentNode) {
        this.parentNode = parentNode
    }

    // endregion

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        for (child in children) {
            child.build(forge, builder)
        }
    }

    // endregion
}
