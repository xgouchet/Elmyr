package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.regex.quantifier.Quantifier

internal class CharacterClassNode(
    override var parentNode: ParentNode?
) : BaseParentNode() {

    private var isClosed: Boolean = false
    private var ongoingRange: Boolean = false

    private var negatedPattern: String = ""
    private val negatedRegex: Regex by lazy { Regex("[$negatedPattern]") }

    // region CharacterClassNode

    internal var isNegation = false

    fun removeLast(): Node {
        return children.removeAt(children.lastIndex)
    }

    fun close() {
        if (ongoingRange) {
            children.add(RawCharNode('-', this))
            ongoingRange = false
        }

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

    // region ParentNode

    override fun handleQuantifier(quantifier: Quantifier) {
        throw UnsupportedOperationException("CharacterClassNode cannot handle quantifiers")
    }

    // endregion

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        check(isClosed) { "Trying to use an unclosed character class" }
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

    override fun check() {
        check(isClosed) { "Character class is not closed" }
        check(children.isNotEmpty()) { "Character class is empty" }
        super.check()
    }

    // endregion
}