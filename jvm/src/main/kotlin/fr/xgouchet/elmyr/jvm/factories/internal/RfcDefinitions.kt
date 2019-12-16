package fr.xgouchet.elmyr.jvm.factories.internal

import fr.xgouchet.elmyr.Case
import fr.xgouchet.elmyr.Forge

internal object RfcDefinitions {

    // region Internal/RFC 3986

    //  URL         = scheme ":" hier-part [ "?" query ] [ "#" fragment ]
    internal fun buildURL(forge: Forge, builder: StringBuilder) {

        builder.append(forge.anElementFrom(KNOWN_URL_SCHEMES))

        builder.append(":")

        val hierarchySize: Int = forge.anInt(1, 255)
        buildHierarchy(forge, builder, hierarchySize)

        if (forge.aBool()) {
            builder.append('?')
            buildQuery(forge, builder)
        }
        if (forge.aBool()) {
            builder.append('#')
            buildFragment(forge, builder)
        }
    }

    //  URI         = scheme ":" hier-part [ "?" query ] [ "#" fragment ]
    internal fun buildURI(forge: Forge, builder: StringBuilder) {

        buildScheme(forge, builder)

        builder.append(":")

        val hierarchySize: Int = forge.anInt(1, 255)
        buildHierarchy(forge, builder, hierarchySize)

        if (forge.aBool()) {
            builder.append('?')
            buildQuery(forge, builder)
        }
        if (forge.aBool()) {
            builder.append('#')
            buildFragment(forge, builder)
        }
    }

    // scheme      = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
    private fun buildScheme(forge: Forge, builder: StringBuilder) {
        val size: Int = forge.anInt(1, 32)
        // Even though we could use upper case too, let's just, not
        builder.append(forge.anAlphabeticalChar(Case.LOWER))

        for (i in 1 until size) {
            builder.append(forge.anElementFrom(RFC3986_SCHEME_CHARS))
        }
    }

    // hier-part   = "//" authority path-abempty / path-absolute / path-rootless / path-empty
    private fun buildHierarchy(forge: Forge, builder: StringBuilder, size: Int) {
        val hasAuthority = forge.aBool()
        if (hasAuthority) {
            builder.append("//")
            buildAuthority(forge, builder)
            buildPathAbsoluteEmpty(forge, builder)
        } else {
            when (forge.anInt(0, 2)) {
                0 -> buildPathAbsolute(forge, builder, size)
                1 -> buildPathRootless(forge, builder)
            }
        }
    }

    // authority   = [ userinfo "@" ] host [ ":" port ]
    private fun buildAuthority(forge: Forge, builder: StringBuilder) {
        // maybe user info
        if (forge.aBool()) {
            buildUserInfo(forge, builder)
            builder.append("@")
        }

        buildHost(forge, builder)

        // maybe port
        if (forge.aBool()) {
            builder.append(":")
            builder.append(forge.anInt(1, 65536))
        }
    }

    // path-abempty  = *( "/" segment )
    private fun buildPathAbsoluteEmpty(forge: Forge, builder: StringBuilder) {
        val segmentCount = forge.aTinyInt()
        for (i in 0..segmentCount) {
            builder.append('/')
            buildSegment(forge, builder)
        }
    }

    // path-absolute = "/" [ segment-nz *( "/" segment ) ]
    private fun buildPathAbsolute(forge: Forge, builder: StringBuilder, size: Int) {
        builder.append('/')
        if (size <= 1) return

        buildPathRootless(forge, builder)
    }

    // path-rootless = segment-nz *( "/" segment )
    private fun buildPathRootless(forge: Forge, builder: StringBuilder) {
        buildSegmentNZ(forge, builder)
        buildPathAbsoluteEmpty(forge, builder)
    }

    // userinfo    = *( unreserved / pct-encoded / sub-delims / ":" )
    private fun buildUserInfo(forge: Forge, builder: StringBuilder) {
        var remainingSize = forge.anInt(0, 64)

        do {
            if (forge.aBool(0.15f) and (remainingSize >= 3)) {
                buildPctEncoded(forge, builder)
                remainingSize -= 3
            } else if (forge.aBool(0.1f)) {
                builder.append(':')
                remainingSize--
            } else if (forge.aBool(0.15f)) {
                builder.append(forge.anElementFrom(RFC3986_SUB_DELIM_CHARS))
                remainingSize--
            } else {
                builder.append(forge.anElementFrom(RFC3986_UNRESERVED_CHARS))
                remainingSize--
            }
        } while (remainingSize > 0)
    }

    // host        = IP-literal / IPv4address / reg-name
    private fun buildHost(forge: Forge, builder: StringBuilder) {
        when (forge.anInt(0, 3)) {
            0 -> buildIPLiteral(forge, builder)
            1 -> buildIPv4Address(forge, builder)
            2 -> buildRegName(forge, builder)
        }
    }

    // IP-literal = "[" ( IPv6address / IPvFuture  ) "]"
    private fun buildIPLiteral(forge: Forge, builder: StringBuilder) {
        // let's drop the IPvFuture for now
        builder.append('[')
        buildIPv6Address(forge, builder)
        builder.append(']')
    }

    // query       = *( pchar / "/" / "?" )
    private fun buildQuery(forge: Forge, builder: StringBuilder) {
        val querySize = forge.anInt(0, 255)
        for (i in 0 until querySize) {
            builder.append(forge.anElementFrom(RFC3986_QUERY_FRAGMENT_CHARS))
        }
    }

    // fragment    = *( pchar / "/" / "?" )
    private fun buildFragment(forge: Forge, builder: StringBuilder) {
        val querySize = forge.anInt(0, 64)
        for (i in 0 until querySize) {
            builder.append(forge.anElementFrom(RFC3986_QUERY_FRAGMENT_CHARS))
        }
    }

    // segment-nz    = 1*pchar
    private fun buildSegmentNZ(forge: Forge, builder: StringBuilder) {
        val segmentSize = forge.anInt(1, 64)
        for (i in 0 until segmentSize) {
            builder.append(forge.anElementFrom(RFC3986_PATH_CHARS))
        }
    }

    // segment       = *pchar
    private fun buildSegment(forge: Forge, builder: StringBuilder) {
        val segmentSize = forge.anInt(0, 64)
        for (i in 0 until segmentSize) {
            builder.append(forge.anElementFrom(RFC3986_PATH_CHARS))
        }
    }

    // reg-name    = *( unreserved / pct-encoded / sub-delims )
    private fun buildRegName(forge: Forge, builder: StringBuilder) {
        val nameSize = forge.anInt(0, 32)
        for (i in 0 until nameSize) {
            if (forge.aBool(0.15f)) {
                buildPctEncoded(forge, builder)
            } else if (forge.aBool(0.1f)) {
                builder.append('.')
            } else if (forge.aBool(0.15f)) {
                builder.append(forge.anElementFrom(RFC3986_SUB_DELIM_CHARS))
            } else {
                builder.append(forge.anElementFrom(RFC3986_UNRESERVED_CHARS))
            }
        }
    }

    // pct-encoded   = "%" HEXDIG HEXDIG
    private fun buildPctEncoded(forge: Forge, builder: StringBuilder) {
        builder.append('%')
        builder.append(forge.anHexadecimalChar())
        builder.append(forge.anHexadecimalChar())
    }

    // endregion

    // region RFC 791 (IPv4 Address)

    // [0-9]{1,3}(.[0-9]{1,3}){3}
    internal fun buildIPv4Address(forge: Forge, builder: StringBuilder) {
        val parts = IntArray(4) { forge.anInt(0, 256) }
        builder.append(parts.joinToString("."))
    }

    // endregion

    // region RFC 4291 (IPv6 Address)

    internal fun buildIPv6Address(forge: Forge, builder: StringBuilder) {
        val full = forge.aBool()
        val v4 = forge.aBool()
        if (v4) {
            if (full) {
                buildIPv6v4FullAddress(forge, builder)
            } else {
                buildIPv6v4CompressedAddress(forge, builder)
            }
        } else {
            if (full) {
                buildIPv6FullAddress(forge, builder)
            } else {
                buildIPv6CompressedAddress(forge, builder)
            }
        }
    }

    private fun buildIPv6FullAddress(forge: Forge, builder: StringBuilder) {
        buildIPv6Hex(forge, builder)
        for (i in 1 until 8) {
            builder.append(':')
            buildIPv6Hex(forge, builder)
        }
    }

    private fun buildIPv6CompressedAddress(forge: Forge, builder: StringBuilder) {
        val groupCount = forge.anInt(1, 7)
        val leadingCount = forge.anInt(0, groupCount + 1)
        val trailingCount = groupCount - leadingCount

        if (leadingCount > 0) {
            buildIPv6Hex(forge, builder)
            for (i in 1 until leadingCount) {
                builder.append(':')
                buildIPv6Hex(forge, builder)
            }
        }
        builder.append("::")
        if (trailingCount > 0) {
            buildIPv6Hex(forge, builder)
            for (i in 1 until trailingCount) {
                builder.append(':')
                buildIPv6Hex(forge, builder)
            }
        }
    }

    private fun buildIPv6v4FullAddress(forge: Forge, builder: StringBuilder) {
        buildIPv6Hex(forge, builder)
        for (i in 1 until 6) {
            builder.append(':')
            buildIPv6Hex(forge, builder)
        }

        builder.append(':')
        buildIPv4Address(forge, builder)
    }

    private fun buildIPv6v4CompressedAddress(forge: Forge, builder: StringBuilder) {
        val groupCount = forge.anInt(1, 5)
        val leadingCount = forge.anInt(0, groupCount + 1)
        val trailingCount = groupCount - leadingCount

        if (leadingCount > 0) {
            buildIPv6Hex(forge, builder)
            for (i in 1 until leadingCount) {
                builder.append(':')
                buildIPv6Hex(forge, builder)
            }
        }
        builder.append("::")
        if (trailingCount > 0) {
            buildIPv6Hex(forge, builder)
            for (i in 1 until trailingCount) {
                builder.append(':')
                buildIPv6Hex(forge, builder)
            }
            builder.append(':')
        }

        buildIPv4Address(forge, builder)
    }

    private fun buildIPv6Hex(forge: Forge, builder: StringBuilder) {
        builder.append(forge.anHexadecimalString(Case.ANY, forge.anInt(1, 5)))
    }

    // endregion

    private val KNOWN_URL_SCHEMES = listOf("file", "ftp", "http", "https", "mailto")

    // scheme      = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
    private val RFC3986_SCHEME_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789+-.".toCharArray()

    // unreserved    = ALPHA / DIGIT / "-" / "." / "_" / "~"
    private val RFC3986_UNRESERVED_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789+-.".toCharArray()

    // gen-delims    = ":" / "/" / "?" / "#" / "[" / "]" / "@"
    private val RFC3986_GEN_DELIM_CHARS = ":/?#[]@".toCharArray()

    // sub-delims    = "!" / "$" / "&" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
    private val RFC3986_SUB_DELIM_CHARS = "!$&'()*+,;=".toCharArray()

    // reserved      = gen-delims / sub-delims
    private val RFC3986_RESERVED_CHARS = RFC3986_GEN_DELIM_CHARS.union(RFC3986_SUB_DELIM_CHARS.toList())

    // pchar         = unreserved / pct-encoded / sub-delims / ":" / "@"
    private val RFC3986_PATH_CHARS = RFC3986_UNRESERVED_CHARS.union(RFC3986_SUB_DELIM_CHARS.toList()).union(listOf(':', '@'))

    // query/fragment       = *( pchar / "/" / "?" )
    private val RFC3986_QUERY_FRAGMENT_CHARS = RFC3986_PATH_CHARS.union(listOf('/', '?'))
}
