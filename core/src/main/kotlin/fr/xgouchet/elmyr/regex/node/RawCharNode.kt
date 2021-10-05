package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

/**
 * Describes a raw character that doesn't have any specific meaning.
 * e.g.: ```/a/```
 */
internal class RawCharNode(
    internal val rawChar: Char,
    internal val escapedChar: String
) : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        builder.append(rawChar)
    }

    override fun toRegex(): String {
        return escapedChar
    }

    // endregion

    // region Object

    override fun toString(): String {
        return "RawCharNode(raw:${rawChar}, esc:$escapedChar)"
    }

    // endregion
}
