package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class PredefinedCharacterClassNode
private constructor(
    internal val shortcut: String,
    private val forging: Forge.() -> Char
) : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        builder.append(forge.forging())
    }

    override fun toRegex(): String {
        return shortcut
    }

    // endregion

    // region Object

    override fun toString(): String {
        return "PredefinedCharacterClassNode(shortcut:'$shortcut')"
    }

    // endregion

    companion object {

        fun digit() = PredefinedCharacterClassNode("\\d") {
            aNumericalChar()
        }

        fun notDigit() = PredefinedCharacterClassNode("\\D") {
            anElementFrom(
                aChar(min = Forge.MIN_PRINTABLE, max = '0'),
                aChar(min = ':', max = Forge.MAX_UTF8)
            )
        }

        fun word() = PredefinedCharacterClassNode("\\w") {
            anElementFrom(
                aChar('a', 'z'),
                aChar('A', 'Z'),
                aChar('0', '9'),
                '_'
            )
        }

        fun notWord() = PredefinedCharacterClassNode("\\W") {
            anElementFrom(
                aChar(min = Forge.MIN_PRINTABLE, max = '0'),
                aChar(min = ':', max = 'A'),
                aChar(min = '[', max = '_'),
                '`',
                aChar(min = '{', max = Forge.MAX_UTF8)
            )
        }

        fun whitespace() = PredefinedCharacterClassNode("\\s") {
            aWhitespaceChar()
        }

        fun notWhitespace() = PredefinedCharacterClassNode("\\S") {
            aChar(min = '\u0021', max = Forge.MAX_UTF8)
        }
    }
}
