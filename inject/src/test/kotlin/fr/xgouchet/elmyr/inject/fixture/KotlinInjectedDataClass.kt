package fr.xgouchet.elmyr.inject.fixture

import fr.xgouchet.elmyr.annotation.Forgery

internal open class KotlinInjectedDataClass {

    @Forgery lateinit var publicBaz: Baz
}
