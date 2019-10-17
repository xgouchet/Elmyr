package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.regex.quantifier.Quantifier

internal class AlternationNode(
    override var parentNode: ParentNode? = null
) : BaseParentNode() {

    // region ParentNode

    override fun handleQuantifier(quantifier: Quantifier) {
        throw UnsupportedOperationException("AlternationNode cannot handle quantifiers")
    }

    // endregion

    // region QuantifiedNode

    override fun build(forge: Forge, builder: StringBuilder) {
        val child = forge.anElementFrom(children)
        child.build(forge, builder)
    }

    // endregion
}