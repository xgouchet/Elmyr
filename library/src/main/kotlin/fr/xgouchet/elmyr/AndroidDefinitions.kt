package fr.xgouchet.elmyr

import fr.xgouchet.elmyr.RFCDefinitions.RFC791_buildIPv4Address

@Suppress("FunctionName", "MagicNumber", "TooManyFunctions")
object AndroidDefinitions {

    private val LABEL_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray()

    private val TLD_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

    private val WORD_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_".toCharArray()

    private val PUNYCODE_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-".toCharArray()

    private val LABEL_EXT_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-".toCharArray()

    private val USERINFO_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789$-_.+!*'(),;?&=".toCharArray()

    private val PATHQUERY_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789;/:@=#~-_.!!*'(),".toCharArray()

    internal fun Android_buildWebUrl(forger: Forger, builder: StringBuilder) {

        Android_buildProtocol(forger, builder)
        if (forger.aBool()) {
            Android_buildUserInfo(forger, builder)
        }

        Android_buildDomainName(forger, builder)
        if (forger.aBool()) {
            Android_buildPortNumber(forger, builder)
        }

        if (forger.aBool()) {
            Android_buildPath(forger, builder)
        }
        if (forger.aBool()) {
            Android_buildQuery(forger, builder)
        }
    }

    private fun Android_buildProtocol(forger: Forger, builder: StringBuilder) {
        builder.append(forger.anElementFrom("http", "https"))
        builder.append("://")
    }

    // username(:password)?@
    private fun Android_buildUserInfo(forger: Forger, builder: StringBuilder) {
        Android_buildUserIdentifier(forger, builder)
        if (forger.aBool()) {
            builder.append(':')
            Android_buildUserPassword(forger, builder)
        }
        builder.append('@')
    }

    private fun Android_buildUserIdentifier(forger: Forger, builder: StringBuilder) {
        val length = forger.anInt(1, 64)
        for (i in 0..length) {
            builder.append(forger.anElementFrom(USERINFO_CHARS))
            // TODO alternative with Encoded chars : %2F%DD…
        }
    }

    private fun Android_buildUserPassword(forger: Forger, builder: StringBuilder) {
        val length = forger.anInt(1, 25)
        for (i in 0..length) {
            builder.append(forger.anElementFrom(USERINFO_CHARS))
            // TODO alternative with Encoded chars : %2F%DD…
        }
    }

    private fun Android_buildDomainName(forger: Forger, builder: StringBuilder) {
        if (forger.aBool()) {
            Android_buildHostName(forger, builder)
        } else {
            RFC791_buildIPv4Address(forger, builder)
        }
    }

    private fun Android_buildHostName(forger: Forger, builder: StringBuilder) {
        val labels = forger.anInt(1, 7)
        for (i in 0 until labels) {
            Android_buildIriLabel(forger, builder)
        }

        Android_buildTLD(forger, builder)
    }

    private fun Android_buildIriLabel(forger: Forger, builder: StringBuilder) {
        // RFC 1035 Section 2.3.4 limits the labels to a maximum 63 octets.
        val innerSize = forger.anInt(0, 62)

        builder.append(forger.anElementFrom(LABEL_CHARS))
        if (innerSize > 0) {
            for (i in 0 until innerSize) {
                builder.append(forger.anElementFrom(LABEL_EXT_CHARS))
            }
            builder.append(forger.anElementFrom(LABEL_CHARS))
        }
        builder.append('.')
    }

    private fun Android_buildTLD(forger: Forger, builder: StringBuilder) {
        if (forger.aBool()) {
            Android_buildPunycodeTLD(forger, builder)
        } else {
            val tldSize = forger.anInt(2, 64)
            for (i in 0 until tldSize) {
                builder.append(forger.anElementFrom(TLD_CHARS))
            }
        }
    }

    private fun Android_buildPunycodeTLD(forger: Forger, builder: StringBuilder) {
        builder.append("xn--")
        val tldSize = forger.anInt(0, 59)
        for (i in 0 until tldSize) {
            builder.append(forger.anElementFrom(PUNYCODE_CHAR))
        }
        builder.append(forger.anElementFrom(WORD_CHAR))
    }

    private fun Android_buildPortNumber(forger: Forger, builder: StringBuilder) {
        builder.append(":")
        builder.append(forger.anInt(1, 65536))
    }

    //  [/\\?]
    //  (
    //      (
    //          [LABELCHAR;/\\?:@&=#~\\-\\.\\+!\\*'\\(\\),_\\$]
    //      )
    //      |
    //      (
    //          %[a-fA-F0-9]{2}
    //      )
    //  )*
    private fun Android_buildPath(forger: Forger, builder: StringBuilder) {
        val pathCount = forger.anInt(0, 8)
        for (i in 0 until pathCount) {
            builder.append('/')
            val pathLength = forger.anInt(1, 32)
            for (j in 0 until pathLength) {
                builder.append(forger.anElementFrom(PATHQUERY_CHARS))
            }
        }
    }

    private fun Android_buildQuery(forger: Forger, builder: StringBuilder) {

        val queryCount = forger.anInt(0, 8)
        for (i in 0 until queryCount) {
            builder.append(if (i == 0) '?' else '&')
            val queryLength = forger.anInt(1, 32)
            for (j in 0 until queryLength) {
                builder.append(forger.anElementFrom(PATHQUERY_CHARS))
            }
        }
    }
}
