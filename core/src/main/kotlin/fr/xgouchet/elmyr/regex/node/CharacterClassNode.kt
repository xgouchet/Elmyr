package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class CharacterClassNode(
    override var parentNode: ParentNode?
) : BaseParentNode() {

    private var isClosed: Boolean = false
    private var isNegated: Boolean = false
    private var ongoingRange: Boolean = false

    private var negatedPattern: String = ""
    private val negatedRegex: Regex by lazy { Regex("[$negatedPattern]") }

    // region CharacterClassNode

    fun close() {
        if (ongoingRange) {
            children.add(RawCharNode('-', this))
            ongoingRange = false
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

    override fun handle(c: Char): Boolean {
        return if (ongoingRange && c != ']') {
            val rangeStart = children.removeAt(children.lastIndex)
            check(rangeStart is RawCharNode) { "Cannot create a range starting with /$rangeStart/" }
            children.add(CharacterRangeNode(rangeStart.rawChar, c, this))
            ongoingRange = false
            true
        } else if (isNegated && c != ']') {
            negatedPattern += c
            true
        } else {
            handleSpecialCharacters(c)
        }
    }

    override fun build(forge: Forge, builder: StringBuilder) {
        check(isClosed) { "Trying to use an unclosed character class" }
        if (isNegated) {
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

    override fun verify() {
        check(isClosed) { "Character class is not closed" }
        super.verify()
    }

    // endregion

    // region Internal

    private fun handleSpecialCharacters(c: Char): Boolean {
        var handled = true
        when (c) {
            '.' -> children.add(RawCharNode(c, this))

            '-' -> if (children.isEmpty()) {
                children.add(RawCharNode(c, this))
            } else {
                ongoingRange = true
            }

            '^' -> if (children.isEmpty()) {
                isNegated = true
            }

            else -> handled = false
        }
        return handled
    }

    // endregion
}