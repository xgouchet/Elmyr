package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

/**
 * Describes a character range.
 * e.g.: ```/[a-z]/```
 */
internal class CharacterRangeNode(
    internal val from: Char,
    internal val to: Char
) : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        builder.append(forge.aChar(from, to))
    }

    // endregion
}
