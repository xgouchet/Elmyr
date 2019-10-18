package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class CharacterClassNode(
    private val isNegation: Boolean
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
            // TODO build Negated pattern properly
            val builder = StringBuilder()
            builder.append('[')
            children.forEach {
                if (it is RawCharNode) {
                    builder.append(it.rawChar)
                }
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