package fr.xgouchet.elmyr.junit5.params

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.StringForgery
import fr.xgouchet.elmyr.annotation.StringForgeryType
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext

internal class StringForgeryParamResolver :
        ForgeryResolver {

    /** @inheritdoc */
    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Boolean {
        val annotated = parameterContext.isAnnotated(StringForgery::class.java)
        return if (annotated) {
            check(parameterContext.parameter.type == String::class.java) {
                "@StringForgery can only be used on a Java or a Kotlin String"
            }
            true
        } else {
            false
        }
    }

    /** @inheritdoc */
    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext,
        forge: Forge
    ): Any? {
        val annotation = parameterContext.findAnnotation(StringForgery::class.java).get()
        return when (annotation.value) {
            StringForgeryType.ALPHABETICAL -> forge.anAlphabeticalString(annotation.case)
            StringForgeryType.ALPHA_NUMERICAL -> forge.anAlphaNumericalString(annotation.case)
            StringForgeryType.NUMERICAL -> forge.aNumericalString()
            StringForgeryType.HEXADECIMAL -> forge.anHexadecimalString(annotation.case)
            StringForgeryType.WHITESPACE -> forge.aWhitespaceString()
            StringForgeryType.ASCII -> forge.anAsciiString()
            StringForgeryType.ASCII_EXTENDED -> forge.anExtendedAsciiString()
        }
    }
}
