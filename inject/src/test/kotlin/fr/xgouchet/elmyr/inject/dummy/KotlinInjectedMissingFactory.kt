package fr.xgouchet.elmyr.inject.dummy

import fr.xgouchet.elmyr.annotation.Forgery

class KotlinInjectedMissingFactory {

    @Forgery
    lateinit var unknownBar: Bar
}
