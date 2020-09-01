package fr.xgouchet.elmyr.junit5.params

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.StringForgery
import fr.xgouchet.elmyr.annotation.StringForgeryType

internal class StringForgeryParamResolver :
    PrimitiveForgeryParamResolver<StringForgery>(
        null,
        java.lang.String::class.java,
        StringForgery::class.java
    ) {

    // region PrimitiveForgeryParamResolver

    override fun forgePrimitive(annotation: StringForgery, forge: Forge): Any? {
        return if (annotation.regex.isNotEmpty()) {
            forge.aStringMatching(annotation.regex)
        } else when (annotation.type) {
            StringForgeryType.ALPHABETICAL -> forge.anAlphabeticalString(annotation.case)
            StringForgeryType.ALPHA_NUMERICAL -> forge.anAlphaNumericalString(annotation.case)
            StringForgeryType.NUMERICAL -> forge.aNumericalString()
            StringForgeryType.HEXADECIMAL -> forge.anHexadecimalString(annotation.case)
            StringForgeryType.WHITESPACE -> forge.aWhitespaceString()
            StringForgeryType.ASCII -> forge.anAsciiString()
            StringForgeryType.ASCII_EXTENDED -> forge.anExtendedAsciiString()
        }
    }

    // endregion
}
