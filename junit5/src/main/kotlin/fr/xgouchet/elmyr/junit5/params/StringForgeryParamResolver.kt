package fr.xgouchet.elmyr.junit5.params

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.StringForgery
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
            StringForgery.Type.ALPHABETICAL -> forge.anAlphabeticalString(annotation.case)
            StringForgery.Type.ALPHA_NUMERICAL -> forge.anAlphaNumericalString(annotation.case)
            StringForgery.Type.NUMERICAL -> forge.aNumericalString()
            StringForgery.Type.HEXADECIMAL -> forge.anHexadecimalString(annotation.case)
            StringForgery.Type.WHITESPACE -> forge.aWhitespaceString()
            StringForgery.Type.ASCII -> forge.anAsciiString()
            StringForgery.Type.ASCII_EXTENDED -> forge.anExtendedAsciiString()
            else -> ""
        }
    }
}
