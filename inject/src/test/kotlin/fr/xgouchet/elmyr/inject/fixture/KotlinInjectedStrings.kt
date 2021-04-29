package fr.xgouchet.elmyr.inject.fixture

import fr.xgouchet.elmyr.Case
import fr.xgouchet.elmyr.annotation.StringForgery
import fr.xgouchet.elmyr.annotation.StringForgeryType

open class KotlinInjectedStrings {

    @StringForgery(StringForgeryType.ALPHABETICAL)
    lateinit var publicAplhaString: String

    @StringForgery(StringForgeryType.ALPHABETICAL, Case.LOWER)
    lateinit var publicAplhaLowerString: String

    @StringForgery(StringForgeryType.ALPHABETICAL, Case.UPPER)
    lateinit var publicAplhaUpperString: String

    @StringForgery(StringForgeryType.ALPHA_NUMERICAL, size = 42)
    lateinit var publicAplhaNumString: String

    @StringForgery(StringForgeryType.NUMERICAL)
    lateinit var publicDigitsString: String

    @StringForgery(StringForgeryType.ASCII)
    internal lateinit var internalAsciiString: String

    @StringForgery(StringForgeryType.ASCII_EXTENDED)
    internal lateinit var internalAsciiExtString: String

    @StringForgery(StringForgeryType.HEXADECIMAL)
    protected lateinit var protectedHexString: String

    @StringForgery(StringForgeryType.WHITESPACE)
    private lateinit var privateWhitespaceString: String

    @StringForgery
    lateinit var publicAlphaStringList: List<String>

    @StringForgery(StringForgeryType.HEXADECIMAL)
    lateinit var publicHexaStringSet: Set<String>

    @StringForgery(StringForgeryType.NUMERICAL)
    lateinit var publicNumStringCollection: Collection<String>

    fun retrieveProtectedHexString(): String = protectedHexString

    fun retrievePrivateWhitespaceString(): String {
        doSomething()
        return privateWhitespaceString
    }

    private fun doSomething() {
    }
}
