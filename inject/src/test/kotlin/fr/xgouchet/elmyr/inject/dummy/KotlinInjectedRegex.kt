package fr.xgouchet.elmyr.inject.dummy

import fr.xgouchet.elmyr.annotation.RegexForgery

open class KotlinInjectedRegex {

    @RegexForgery("[a-z]+")
    lateinit var publicAlphaString: String

    @RegexForgery("[0-9]+")
    internal lateinit var internalDigitsString: String

    @RegexForgery("([a-zA-z0-9]{4})*[a-zA-z0-9]{2}==")
    protected lateinit var protectedBase64String: String

    @RegexForgery("0\\d(-\\d\\d){4}")
    private lateinit var privatePhoneNumber: String

    fun retrieveProtectedBase64String(): String = protectedBase64String

    fun retrievePrivatePhoneNumber(): String {
        doSomething()
        return privatePhoneNumber
    }

    private fun doSomething() {
    }
}
