package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class PredefinedCharacterClassNode
private constructor(
    private val forging: Forge.() -> Char
) : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        builder.append(forge.forging())
    }

    // endregion

    companion object {

        fun digit() = PredefinedCharacterClassNode() {
            aNumericalChar()
        }

        fun notDigit() = PredefinedCharacterClassNode() {
            anElementFrom(
                    aChar(min = Forge.MIN_PRINTABLE, max = '0'),
                    aChar(min = ':', max = Forge.MAX_UTF8)
            )
        }

        fun word() = PredefinedCharacterClassNode() {
            anElementFrom(
                    aChar('a', 'z'),
                    aChar('A', 'Z'),
                    aChar('0', '9'),
                    '_')
        }

        fun notWord() = PredefinedCharacterClassNode() {
            anElementFrom(
                    aChar(min = Forge.MIN_PRINTABLE, max = '0'),
                    aChar(min = ':', max = 'A'),
                    aChar(min = '[', max = '_'),
                    '`',
                    aChar(min = '{', max = Forge.MAX_UTF8)
            )
        }

        fun whitespace() = PredefinedCharacterClassNode() {
            aWhitespaceChar()
        }

        fun notWhitespace() = PredefinedCharacterClassNode() {
            aChar(min = '\u0021', max = Forge.MAX_UTF8)
        }
    }
}