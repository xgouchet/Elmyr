package fr.xgouchet.elmyr.inject.dummy

import fr.xgouchet.elmyr.annotation.Forgery

class KotlinInjectedImmutableVal {

    @Forgery
    val finalFoo42: Foo = Foo(42)

    @Forgery
    val finalFoo13: Foo = Foo(13)
}
