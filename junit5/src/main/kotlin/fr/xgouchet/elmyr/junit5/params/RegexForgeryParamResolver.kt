package fr.xgouchet.elmyr.junit5.params

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.RegexForgery
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext

internal class RegexForgeryParamResolver :
        ForgeryResolver {

    /** @inheritdoc */
    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Boolean {
        val annotated = parameterContext.isAnnotated(RegexForgery::class.java)
        return if (annotated) {
            check(parameterContext.parameter.type == String::class.java) {
                "@RegexForgery can only be used on a Java or a Kotlin String"
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
        val annotation = parameterContext.findAnnotation(RegexForgery::class.java).get()
        return forge.aStringMatching(annotation.value)
    }
}
