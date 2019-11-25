package fr.xgouchet.elmyr.inject.dummy

import fr.xgouchet.elmyr.annotation.Forgery

class KotlinInjectedUnknownGeneric {

    @Forgery
    lateinit var unknownGeneric: Comparable<Foo>
}
