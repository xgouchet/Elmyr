package fr.xgouchet.elmyr.inject.fixture

import fr.xgouchet.elmyr.annotation.Forgery

class KotlinInjectedUnknownGeneric {

    @Forgery
    lateinit var unknownGeneric: Comparable<Foo>
}
