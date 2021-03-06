package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class RawCharNode(
    val rawChar: Char
) : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        builder.append(rawChar)
    }

    // endregion
}
