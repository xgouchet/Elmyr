package fr.xgouchet.elmyr.inject.fixture

import fr.xgouchet.elmyr.annotation.Forgery

internal class KotlinInjectedUnknownGeneric {

    @Forgery
    lateinit var unknownGeneric: Comparable<Foo>
}
