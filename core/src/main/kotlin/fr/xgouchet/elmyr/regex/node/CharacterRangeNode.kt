package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

/**
 * Describes a character range.
 * e.g.: ```/[a-z]/```
 */
internal class CharacterRangeNode(
    internal val from: RawCharNode,
    internal val to: RawCharNode
) : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {

        builder.append(forge.aChar(from.rawChar, to.rawChar))
    }

    override fun toRegex(): String {
        return "${from.toRegex()}-${to.toRegex()}"
    }

    // endregion

    // region Object

    override fun toString(): String {
        return "CharacterRangeNode(from:$from, to:$to)"
    }

    // endregion
}
