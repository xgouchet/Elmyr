package fr.xgouchet.elmyr.inject.fixture

import fr.xgouchet.elmyr.annotation.Forgery

class KotlinInjectedMissingFactory {

    @Forgery
    lateinit var unknownSpam: Spam
}
