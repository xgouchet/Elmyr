package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.jvm.factories.internal.RfcDefinitions
import java.net.URL

/**
 * A [ForgeryFactory] that will generate a [URL] instance.
 * The URI will follow the [RFC-3986](https://tools.ietf.org/html/rfc3986)
 */
class UrlForgeryFactory :
        ForgeryFactory<URL> {

    override fun getForgery(forge: Forge): URL {
        val builder = StringBuilder()
        RfcDefinitions.buildURL(forge, builder)
        return URL(builder.toString())
    }
}
