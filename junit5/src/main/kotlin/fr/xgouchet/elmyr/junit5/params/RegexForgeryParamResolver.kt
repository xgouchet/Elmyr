package fr.xgouchet.elmyr.junit5.params

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.RegexForgery

internal class RegexForgeryParamResolver :
    PrimitiveForgeryParamResolver<RegexForgery>(
        null,
        java.lang.String::class.java,
        RegexForgery::class.java
    ) {

    // region PrimitiveForgeryParamResolver

    override fun forgePrimitive(annotation: RegexForgery, forge: Forge): Any? {
        return forge.aStringMatching(annotation.value)
    }

    // endregion
}
