package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class CharacterRangeNode(
    private val from: Char,
    private val to: Char
) : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        builder.append(forge.aChar(from, to))
    }

    // endregion
}
