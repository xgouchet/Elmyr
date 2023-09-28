package fr.xgouchet.elmyr.inject.fixture

import fr.xgouchet.elmyr.annotation.Forgery

internal open class KotlinInjected {

    @Forgery lateinit var publicFoo: Foo
    @Forgery internal lateinit var internalFoo: Foo
    @Forgery protected lateinit var protectedFoo: Foo
    @Forgery private lateinit var privateFoo: Foo

    fun retrieveProtectedFoo(): Foo = protectedFoo

    fun retrievePrivateFoo(): Foo {
        doSomething()
        return privateFoo
    }

    private fun doSomething() {
    }
}
