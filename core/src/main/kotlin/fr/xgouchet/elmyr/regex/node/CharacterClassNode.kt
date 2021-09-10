package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

/**
 * Describes a character class.
 * e.g.: ```/[abc_]/```
 */
internal class CharacterClassNode(
    internal val isNegation: Boolean
) : BaseParentNode() {

    private var isClosed: Boolean = false

    private var negatedPattern: String = ""
    private val negatedRegex: Regex by lazy { Regex("[$negatedPattern]") }

    // region CharacterClassNode

    fun removeLast(): Node {
        return children.removeAt(children.lastIndex)
    }

    fun close() {
        check(children.isNotEmpty()) { "Character class is empty" }

        if (isNegation) {
            val builder = StringBuilder()
            builder.append('[')
            children.forEach {
                val repr = when (it) {
                    is RawCharNode -> it.escapedChar
                    is CharacterRangeNode -> "${it.from}-${it.to}"
                    is PredefinedCharacterClassNode -> "\\${it.shortcut}"
                    else -> TODO("Can't handle $it")
                }
                builder.append(repr)
            }
            builder.append(']')
            negatedPattern = builder.toString()
        }

        isClosed = true
    }

    // endregion

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        if (isNegation) {
            var char: Char
            do {
                char = forge.aChar()
            } while (negatedRegex.matches("$char"))
            builder.append(char)
        } else {
            val child = forge.anElementFrom(children)
            child.build(forge, builder)
        }
    }

    // endregion
}
