package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.regex.quantifier.Quantifier

internal class GroupNode(
    override var parentNode: ParentNode?
) : BaseParentNode() {

    var referenceValue: String = ""

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
        val localBuilder = StringBuilder()
        children.first().build(forge, localBuilder)
        referenceValue = localBuilder.toString()
        builder.append(localBuilder.toString())
    }

    // endregion
}