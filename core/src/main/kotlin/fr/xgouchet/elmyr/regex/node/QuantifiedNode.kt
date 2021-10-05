package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.regex.quantifier.Quantifier

/**
 * Describes a Quantify modifier.
 * e.g.: ```/a+b*c?/```
 */
internal class QuantifiedNode(
    private val node: Node,
    private val quantifier: Quantifier
) : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        val repeats = quantifier.getQuantity(forge)

        for (i in 0 until repeats) {
            node.build(forge, builder)
        }
    }

    override fun toRegex(): String {
        return "${node.toRegex()}$quantifier"
    }


    // endregion

    // region Object

    override fun toString(): String {
        return "QuantifiedNode(node:$node, quantifier:$quantifier)"
    }

    // endregion
}
