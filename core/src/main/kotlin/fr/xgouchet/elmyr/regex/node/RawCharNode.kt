package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class RawCharNode(
    val rawChar: Char,
    override var parentNode: ParentNode?
) : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        builder.append(rawChar)
    }

    override fun check() {}

    // endregion
}
