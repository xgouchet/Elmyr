package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class CharacterRangeNode(
    val from: Char,
    val to: Char,
    override var parentNode: ParentNode?
) : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        builder.append(forge.aChar(from, to))
    }

    override fun verify() {
        check(from < to) { "Illegal character range /$from-$to/" }
    }

    // endregion
}