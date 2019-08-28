package fr.xgouchet.elmyr

import java.lang.Math.min

@Suppress("LargeClass", "TooManyFunctions", "ComplexMethod", "MagicNumber", "FunctionName")
object RFCDefinitions {

    // region RFC 1035 (domain names)

    const val MAX_DOMAIN_LENGTH = 255

    // <domain> ::= <subdomain> | " "
    internal fun RFC1035_buildDomain(forger: Forger, builder: StringBuilder, size: Int? = null) {
        val resultSize = size ?: forger.anInt(0, MAX_DOMAIN_LENGTH) + 1

        RFC1035_buildSubdomain(forger, builder, resultSize)
    }

    // <subdomain> ::= <label> | <subdomain> "." <label>
    private fun RFC1035_buildSubdomain(forger: Forger, builder: StringBuilder, size: Int) {
        val labelSize = forger.anInt(1, 64)
        val justLabel = (labelSize >= size - 2) or forger.aBool(size.toFloat() / 255.0f)

        if (justLabel) {
            RFC1035_buildLabel(forger, builder, size)
        } else {
            RFC1035_buildSubdomain(forger, builder, size - labelSize - 1)
            builder.append('.')
            RFC1035_buildLabel(forger, builder, labelSize)
        }
    }

    // <label> ::= <letter> [ [ <ldh-str> ] <let-dig> ]
    // <ldh-str> ::= <let-dig-hyp> | <let-dig-hyp> <ldh-str>
    // <let-dig-hyp> ::= <let-dig> | "-"
    // <let-dig> ::= <letter> | <digit>
    private fun RFC1035_buildLabel(forger: Forger, builder: StringBuilder, size: Int) {
        builder.append(forger.anAlphabeticalChar())
        if (size > 2) {
            builder.append(forger.aStringMatching("[a-zA-Z0-9-]{${size - 2}}"))
        }
        builder.append(forger.anAlphaNumericalChar())
    }

    // endregion

    // region RFC 822 (email address pre 2001)

    const val MAX_LOCALPART_LENGTH = 64
    const val MAX_EMAIL_LENGTH = 254

    private val RFC822_ATOM_CHARS = "!#\$%&'*+-/0123456789=?ABCDEFGHIJKLMNOPQRSTUVWXYZ^_`abscdefghijklmnopqrstuvwxyz{|}~"
            .toCharArray()

    // addr-spec   =  local-part "@" domain
    internal fun RFC822_buildEmail(forger: Forger, builder: StringBuilder, size: Int? = null): String {
        val emailSize = size ?: MAX_EMAIL_LENGTH
        val localSize = min(forger.anInt(1, MAX_LOCALPART_LENGTH), emailSize - 2)
        val domainSize = forger.anInt(1, min(emailSize - localSize - 1, MAX_DOMAIN_LENGTH))

        RFC822_buildLocalPart(forger, builder, localSize)
        builder.append("@")
        RFC1035_buildDomain(forger, builder, domainSize)

        return builder.toString()
    }

    // local-part  =  word *("." word)
    private fun RFC822_buildLocalPart(forger: Forger, builder: StringBuilder, size: Int) {
        var remainingSize = size
        var wordSize: Int
        var started = false

        do {
            if (started) {
                if (remainingSize > 1) {
                    builder.append('.')
                }
                remainingSize--
            }
            wordSize = min(forger.anInt(1, 64), remainingSize)
            RFC822_buildWord(forger, builder, wordSize)
            remainingSize -= wordSize
            started = true
        } while (remainingSize > 0)
    }

    // word = atom | quoted-string
    // atom = *<any CHAR except specials, SPACE and CTLs>
    private fun RFC822_buildWord(forger: Forger, builder: StringBuilder, size: Int) {
        // TODO Quoted strings
        val array = CharArray(size) { forger.anElementFrom(RFC822_ATOM_CHARS) }
        builder.append(array)
    }

    // endregion

    // region RFC 2822 (email address)

    private val RFC2822_ATEXT_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabscdefghijklmnopqrstuvwxyz!#$%&'*+-/=?^_`{|}~"
            .toCharArray()

    // addr-spec       =       local-part "@" domain
    internal fun RFC2822_buildEmail(forger: Forger, builder: StringBuilder, size: Int? = null): String {
        val emailSize = size ?: MAX_EMAIL_LENGTH
        val localSize = min(forger.anInt(1, 64), emailSize - 2)
        val domainSize = forger.anInt(1, min(emailSize - localSize - 1, MAX_DOMAIN_LENGTH))

        RFC2822_buildLocalPart(forger, builder, localSize)
        builder.append("@")
        RFC2822_buildDomain(forger, builder, domainSize)

        return builder.toString()
    }

    // local-part      =       dot-atom / quoted-string / obs-local-part
    private fun RFC2822_buildLocalPart(forger: Forger, builder: StringBuilder, size: Int) {
        RFC2822_buildDotAtom(forger, builder, size)
        // TODO Quotes String and ObsLocalPart
    }

    // For now we don't implement comments / folding whitespace
    // dot-atom        =       [CFWS] dot-atom-text [CFWS]
    // dot-atom-text   =       1*atext *("." 1*atext)
    private fun RFC2822_buildDotAtom(forger: Forger, builder: StringBuilder, size: Int) {
        val wordSize = forger.anInt(1, 64)
        val singleWord = (wordSize >= size - 2) or forger.aBool()

        if (singleWord) {
            val array = CharArray(size) { forger.anElementFrom(RFC2822_ATEXT_CHARS) }
            builder.append(array)
        } else {
            val array = CharArray(wordSize) { forger.anElementFrom(RFC2822_ATEXT_CHARS) }
            builder.append(array)
            builder.append('.')
            RFC2822_buildDotAtom(forger, builder, size - wordSize - 1)
        }
    }

    // domain          =       dot-atom / domain-literal / obs-domain
    private fun RFC2822_buildDomain(forger: Forger, builder: StringBuilder, size: Int) {
        if (forger.aBool()) {
            RFC2822_buildDotAtom(forger, builder, size)
        } else {
            RFC2822_buildDomainLiteral(forger, builder, size)
        }
    }

    // For now we don't implement comments / folding whitespace
    // domain-literal  =     "[" *([FWS] dcontent) [FWS] "]"
    // dcontent        =       dtext / quoted-pair
    private fun RFC2822_buildDomainLiteral(forger: Forger, builder: StringBuilder, size: Int) {
        // although the RFC allow a lot of dtext in here eg : me@[mycomputer], we'll limit the literal to ip address
        builder.append('[')
        RFC5321_buildAddressLiteral(forger, builder, size - 2)
        builder.append(']')
    }

    //  address-literal  = "[" ( IPv4-address-literal | IPv6-address-literal | General-address-literal ) "]"
    private fun RFC5321_buildAddressLiteral(forger: Forger, builder: StringBuilder, size: Int) {

        if (size <= 16) {
            RFC791_buildIPv4Address(forger, builder)
        } else {
            builder.append("IPv6:")
            RFC4291_buildIPv6Address(forger, builder)
        }

    }

    // endregion

    // region RFC 791 / 4291 (IP address)

    // [0-9]{1,3}(.[0-9]{1,3}){3}
    internal fun RFC791_buildIPv4Address(forger: Forger, builder: StringBuilder) {
        val parts = IntArray(4) { forger.anInt(0, 256) }
        builder.append(parts.joinToString("."))
    }

    internal fun RFC4291_buildIPv6Address(forger: Forger, builder: StringBuilder) {
        val full = forger.aBool()
        val v4 = forger.aBool()
        if (v4) {
            if (full) {
                RFC4291_buildIPv6v4FullAddress(forger, builder)
            } else {
                RFC4291_buildIPv6v4CompressedAddress(forger, builder)
            }
        } else {
            if (full) {
                RFC4291_buildIPv6FullAddress(forger, builder)
            } else {
                RFC4291_buildIPv6CompressedAddress(forger, builder)
            }
        }
    }

    private fun RFC4291_buildIPv6FullAddress(forger: Forger, builder: StringBuilder) {
        RFC4291_buildIPv6Hex(forger, builder)
        for (i in 1 until 8) {
            builder.append(':')
            RFC4291_buildIPv6Hex(forger, builder)
        }
    }

    private fun RFC4291_buildIPv6CompressedAddress(forger: Forger, builder: StringBuilder) {
        val groupCount = forger.anInt(1, 7)
        val leadingCount = forger.anInt(0, groupCount + 1)
        val trailingCount = groupCount - leadingCount

        if (leadingCount > 0) {
            RFC4291_buildIPv6Hex(forger, builder)
            for (i in 1 until leadingCount) {
                builder.append(':')
                RFC4291_buildIPv6Hex(forger, builder)
            }
        }
        builder.append("::")
        if (trailingCount > 0) {
            RFC4291_buildIPv6Hex(forger, builder)
            for (i in 1 until trailingCount) {
                builder.append(':')
                RFC4291_buildIPv6Hex(forger, builder)
            }
        }
    }

    private fun RFC4291_buildIPv6v4FullAddress(forger: Forger, builder: StringBuilder) {
        RFC4291_buildIPv6Hex(forger, builder)
        for (i in 1 until 6) {
            builder.append(':')
            RFC4291_buildIPv6Hex(forger, builder)
        }

        builder.append(':')
        RFC791_buildIPv4Address(forger, builder)
    }

    private fun RFC4291_buildIPv6v4CompressedAddress(forger: Forger, builder: StringBuilder) {
        val groupCount = forger.anInt(1, 5)
        val leadingCount = forger.anInt(0, groupCount + 1)
        val trailingCount = groupCount - leadingCount

        if (leadingCount > 0) {
            RFC4291_buildIPv6Hex(forger, builder)
            for (i in 1 until leadingCount) {
                builder.append(':')
                RFC4291_buildIPv6Hex(forger, builder)
            }
        }
        builder.append("::")
        if (trailingCount > 0) {
            RFC4291_buildIPv6Hex(forger, builder)
            for (i in 1 until trailingCount) {
                builder.append(':')
                RFC4291_buildIPv6Hex(forger, builder)
            }
            builder.append(':')
        }

        RFC791_buildIPv4Address(forger, builder)
    }

    private fun RFC4291_buildIPv6Hex(forger: Forger, builder: StringBuilder) {
        builder.append(forger.anHexadecimalString(Case.ANY, forger.anInt(1, 5)))
    }
    // endregion

    // region RFC 3986 (URI)

    // Known URL schemes
    internal val KNOWN_URL_SCHEMES = listOf("file", "ftp", "http", "https", "mailto")

    // scheme      = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
    private val RFC3986_SCHEME_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789+-.".toCharArray()

    // unreserved    = ALPHA / DIGIT / "-" / "." / "_" / "~"
    private val RFC3986_UNRESERVED_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789+-.".toCharArray()

    // gen-delims    = ":" / "/" / "?" / "#" / "[" / "]" / "@"
    private val RFC3986_GEN_DELIM_CHARS = ":/?#[]@".toCharArray()

    // sub-delims    = "!" / "$" / "&" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
    private val RFC3986_SUB_DELIM_CHARS = "!$&'()*+,;=".toCharArray()

    // reserved      = gen-delims / sub-delims
//    private val RFC3986_RESERVED_CHARS = RFC3986_GEN_DELIM_CHARS.union(RFC3986_SUB_DELIM_CHARS.toList())

    // pchar         = unreserved / pct-encoded / sub-delims / ":" / "@"
    private val RFC3986_PATH_CHARS = RFC3986_UNRESERVED_CHARS.union(RFC3986_SUB_DELIM_CHARS.toList()).union(listOf(':', '@'))

    // query/fragment       = *( pchar / "/" / "?" )
    private val RFC3986_QUERY_FRAGMENT_CHARS = RFC3986_PATH_CHARS.union(listOf('/', '?'))

    //  URI         = scheme ":" hier-part [ "?" query ] [ "#" fragment ]
    internal fun RFC3986_buildURI(forger: Forger, builder: StringBuilder) {

        RFC3986_buildScheme(forger, builder)

        builder.append(":")

        val hierarchySize: Int = forger.anInt(1, 255)
        RFC3986_buildHierarchy(forger, builder, hierarchySize)

        if (forger.aBool()) {
            builder.append('?')
            RFC3986_buildQuery(forger, builder)
        }
        if (forger.aBool()) {
            builder.append('#')
            RFC3986_buildFragment(forger, builder)
        }
    }

    //  URL         = scheme ":" hier-part [ "?" query ] [ "#" fragment ]
    internal fun RFC3986_buildURL(forger: Forger, builder: StringBuilder, scheme: String? = null) {

        if (scheme.isNullOrBlank()) {
            builder.append(forger.anElementFrom(KNOWN_URL_SCHEMES))
        } else {
            builder.append(scheme)
        }

        builder.append(":")

        val hierarchySize: Int = forger.anInt(1, 255)
        RFC3986_buildHierarchy(forger, builder, hierarchySize)

        if (forger.aBool()) {
            // TODO add query ?foo=42&bar=toto
        }
        if (forger.aBool()) {
            // TODO add fragment #spam
        }
    }

    // scheme      = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
    private fun RFC3986_buildScheme(forger: Forger, builder: StringBuilder) {
        val size: Int = forger.anInt(1, 32)
        // Even though we could use upper case too, let's just, not
        builder.append(forger.anAlphabeticalChar(Case.LOWER))

        for (i in 1 until size) {
            builder.append(forger.anElementFrom(RFC3986_SCHEME_CHARS))
        }
    }

    // hier-part   = "//" authority path-abempty / path-absolute / path-rootless / path-empty
    private fun RFC3986_buildHierarchy(forger: Forger, builder: StringBuilder, size: Int) {
        val hasAuthority = forger.aBool()
        if (hasAuthority) {
            builder.append("//")
            RFC3986_buildAuthority(forger, builder)
            RFC3986_buildPathAbsoluteEmpty(forger, builder)
        } else {
            when (forger.anInt(0, 2)) {
                0 -> RFC3986_buildPathAbsolute(forger, builder, size)
                1 -> RFC3986_buildPathRootless(forger, builder)
            }
        }
    }

    // authority   = [ userinfo "@" ] host [ ":" port ]
    private fun RFC3986_buildAuthority(forger: Forger, builder: StringBuilder) {
        // maybe user info
        if (forger.aBool(0.25f)) {
            RFC3986_buildUserInfo(forger, builder)
            builder.append("@")
        }

        RFC3986_buildHost(forger, builder)

        // maybe port
        if (forger.aBool(0.25f)) {
            builder.append(":")
            builder.append(forger.anInt(1, 65536))
        }
    }

    // path-abempty  = *( "/" segment )
    private fun RFC3986_buildPathAbsoluteEmpty(forger: Forger, builder: StringBuilder) {
        val segmentCount = forger.aTinyInt()
        for (i in 0..segmentCount) {
            builder.append('/')
            RFC3986_buildSegment(forger, builder)
        }
    }

    // path-absolute = "/" [ segment-nz *( "/" segment ) ]
    private fun RFC3986_buildPathAbsolute(forger: Forger, builder: StringBuilder, size: Int) {
        builder.append('/')
        if (size <= 1) return

        RFC3986_buildPathRootless(forger, builder)
    }

    // path-rootless = segment-nz *( "/" segment )
    private fun RFC3986_buildPathRootless(forger: Forger, builder: StringBuilder) {
        RFC3986_buildSegmentNZ(forger, builder)
        RFC3986_buildPathAbsoluteEmpty(forger, builder)
    }

    // userinfo    = *( unreserved / pct-encoded / sub-delims / ":" )
    private fun RFC3986_buildUserInfo(forger: Forger, builder: StringBuilder) {
        var remainingSize = forger.anInt(0, 64)

        do {
            if (forger.aBool(0.15f) and (remainingSize >= 3)) {
                RFC3986_buildPctEncoded(forger, builder)
                remainingSize -= 3
            } else if (forger.aBool(0.1f)) {
                builder.append(':')
                remainingSize--
            } else if (forger.aBool(0.15f)) {
                builder.append(forger.anElementFrom(RFC3986_SUB_DELIM_CHARS))
                remainingSize--
            } else {
                builder.append(forger.anElementFrom(RFC3986_UNRESERVED_CHARS))
                remainingSize--
            }
        } while (remainingSize > 0)
    }

    // host        = IP-literal / IPv4address / reg-name
    private fun RFC3986_buildHost(forger: Forger, builder: StringBuilder) {
        when (forger.anInt(0, 3)) {
            0 -> RFC3986_buildIPLiteral(forger, builder)
            1 -> RFC791_buildIPv4Address(forger, builder)
            2 -> RFC3986_buildRegName(forger, builder)
        }
    }

    // IP-literal = "[" ( IPv6address / IPvFuture  ) "]"
    private fun RFC3986_buildIPLiteral(forger: Forger, builder: StringBuilder) {
        // let's drop the IPvFuture for now
        builder.append('[')
        RFC4291_buildIPv6Address(forger, builder)
        builder.append(']')
    }

    // query       = *( pchar / "/" / "?" )
    private fun RFC3986_buildQuery(forger: Forger, builder: StringBuilder) {
        val querySize = forger.anInt(0, 255)
        for (i in 0 until querySize) {
            builder.append(forger.anElementFrom(RFC3986_QUERY_FRAGMENT_CHARS))
        }
    }

    // fragment    = *( pchar / "/" / "?" )
    private fun RFC3986_buildFragment(forger: Forger, builder: StringBuilder) {
        val querySize = forger.anInt(0, 64)
        for (i in 0 until querySize) {
            builder.append(forger.anElementFrom(RFC3986_QUERY_FRAGMENT_CHARS))
        }
    }

    // segment-nz    = 1*pchar
    private fun RFC3986_buildSegmentNZ(forger: Forger, builder: StringBuilder) {
        val segmentSize = forger.anInt(1, 64)
        for (i in 0 until segmentSize) {
            builder.append(forger.anElementFrom(RFC3986_PATH_CHARS))
        }
    }

    // segment       = *pchar
    private fun RFC3986_buildSegment(forger: Forger, builder: StringBuilder) {
        val segmentSize = forger.anInt(0, 64)
        for (i in 0 until segmentSize) {
            builder.append(forger.anElementFrom(RFC3986_PATH_CHARS))
        }
    }

    // reg-name    = *( unreserved / pct-encoded / sub-delims )
    private fun RFC3986_buildRegName(forger: Forger, builder: StringBuilder) {
        val nameSize = forger.anInt(0, 32)
        for (i in 0 until nameSize) {
            if (forger.aBool(0.15f)) {
                RFC3986_buildPctEncoded(forger, builder)
            } else if (forger.aBool(0.1f)) {
                builder.append('.')
            } else if (forger.aBool(0.15f)) {
                builder.append(forger.anElementFrom(RFC3986_SUB_DELIM_CHARS))
            } else {
                builder.append(forger.anElementFrom(RFC3986_UNRESERVED_CHARS))
            }
        }
    }

    // pct-encoded   = "%" HEXDIG HEXDIG
    private fun RFC3986_buildPctEncoded(forger: Forger, builder: StringBuilder) {
        builder.append('%')
        builder.append(forger.anHexadecimalChar())
        builder.append(forger.anHexadecimalChar())
    }

    // endregion
}
