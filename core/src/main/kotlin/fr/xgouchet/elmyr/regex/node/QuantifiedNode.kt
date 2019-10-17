package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.regex.quantifier.Quantifier

internal class QuantifiedNode(
    private val node: Node,
    private val quantifier: Quantifier,
    override var parentNode: ParentNode?
) : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        val repeats = quantifier.getQuantity(forge)

        for (i in 0 until repeats) {
            node.build(forge, builder)
        }
    }

    override fun check() {}

    // endregion
}