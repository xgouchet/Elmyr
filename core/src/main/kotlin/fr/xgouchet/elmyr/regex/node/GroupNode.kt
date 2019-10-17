package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.regex.quantifier.Quantifier

internal class GroupNode(
    override var parentNode: ParentNode?
) : BaseParentNode() {

    // region ParentNode

    override fun handleQuantifier(quantifier: Quantifier) {
        throw UnsupportedOperationException("CharacterClassNode cannot handle quantifiers")
    }

    // endregion

    // region Node

    override fun check() {
        check(children.size == 1) { "Illegal group construction" }
        super.check()
    }

    override fun build(forge: Forge, builder: StringBuilder) {
        children.first().build(forge, builder)
    }

    // endregion
}