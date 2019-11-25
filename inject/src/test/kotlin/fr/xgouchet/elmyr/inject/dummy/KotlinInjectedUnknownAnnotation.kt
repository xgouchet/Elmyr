package fr.xgouchet.elmyr.inject.dummy

import fr.xgouchet.elmyr.annotation.Forgery

class KotlinInjectedUnknownAnnotation {

    @Forgery
    lateinit var unknownSpam: Spam
}
