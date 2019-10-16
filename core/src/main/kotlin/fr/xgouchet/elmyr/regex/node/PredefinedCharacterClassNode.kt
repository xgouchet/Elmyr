package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class PredefinedCharacterClassNode
private constructor(
    override var parentNode: ParentNode?,
    private val forging: Forge.() -> Char
) : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        builder.append(forge.forging())
    }

    override fun verify() {}

    // endregion

    companion object {

        fun digit(parentNode: ParentNode?) = PredefinedCharacterClassNode(parentNode) {
            aNumericalChar()
        }

        fun notDigit(parentNode: ParentNode?) = PredefinedCharacterClassNode(parentNode) {
            anElementFrom(
                    aChar(min = Forge.MIN_PRINTABLE, max = '0'),
                    aChar(min = ':', max = Forge.MAX_UTF8)
            )
        }

        fun word(parentNode: ParentNode?) = PredefinedCharacterClassNode(parentNode) {
            anElementFrom(
                    aChar('a', 'z'),
                    aChar('A', 'Z'),
                    aChar('0', '9'),
                    '_')
        }

        fun notWord(parentNode: ParentNode?) = PredefinedCharacterClassNode(parentNode) {
            anElementFrom(
                    aChar(min = Forge.MIN_PRINTABLE, max = '0'),
                    aChar(min = ':', max = 'A'),
                    aChar(min = '[', max = '_'),
                    '`',
                    aChar(min = '{', max = Forge.MAX_UTF8)
            )
        }

        fun whitespace(parentNode: ParentNode?) = PredefinedCharacterClassNode(parentNode) {
            aWhitespaceChar()
        }

        fun notWhitespace(parentNode: ParentNode?) = PredefinedCharacterClassNode(parentNode) {
            aChar(min = '\u0021', max = Forge.MAX_UTF8)
        }
    }
}