package fr.xgouchet.elmyr

import java.lang.Math.min

object RFCDefinitions {

    const val MAX_LOCALPART_LENGTH = 64
    const val MAX_DOMAIN_LENGTH = 255
    const val MAX_EMAIL_LENGTH = 254

    @JvmField
    val RFC2822_ATEXT_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabscdefghijklmnopqrstuvwxyz!#$%&'*+-/=?^_`{|}~".toCharArray()

    @JvmField
    val RFC822_ATOM_CHARS = "!#\$%&'*+-/0123456789=?ABCDEFGHIJKLMNOPQRSTUVWXYZ^_`abscdefghijklmnopqrstuvwxyz{|}~".toCharArray()

    // region RFC 1035 (domain names)

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

    // addr-spec   =  local-part "@" domain
    internal fun RFC822_buildEmail(forger: Forger, builder: StringBuilder, size: Int? = null): String {
        val emailSize = size ?: MAX_EMAIL_LENGTH
        val localSize = min(forger.anInt(1, 64), emailSize - 2)
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
                builder.append('.')
                remainingSize--
            }
            wordSize = min(forger.anInt(1, 64), remainingSize)
            RFC822_buildWord(forger, builder, wordSize)
            remainingSize -= wordSize
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
        val withv4 = forger.aBool()
        if (withv4) {
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
            for (i in 1 until leadingCount) {
                builder.append(':')
                RFC4291_buildIPv6Hex(forger, builder)
            }
        }
    }

    private fun RFC4291_buildIPv6v4FullAddress(forger: Forger, builder: StringBuilder) {
        RFC4291_buildIPv6Hex(forger, builder)
        for (i in 1 until 5) {
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
            for (i in 1 until leadingCount) {
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
}