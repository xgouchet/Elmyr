package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.jvm.factories.internal.RfcDefinitions
import java.net.URI

/**
 * A [ForgeryFactory] that will generate a [URI] instance.
 * The URI will follow the [RFC-3986](https://tools.ietf.org/html/rfc3986)
 */
class UriForgeryFactory :
        ForgeryFactory<URI> {

    override fun getForgery(forge: Forge): URI {
        val builder = StringBuilder()
        RfcDefinitions.buildURI(forge, builder)
        return URI(builder.toString())
    }
}
