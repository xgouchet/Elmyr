package fr.xgouchet.elmyr.inject.dummy

import fr.xgouchet.elmyr.annotation.Forgery

class KotlinInjectedChild :
    KotlinInjected() {

    @Forgery lateinit var childFoo: Foo
}