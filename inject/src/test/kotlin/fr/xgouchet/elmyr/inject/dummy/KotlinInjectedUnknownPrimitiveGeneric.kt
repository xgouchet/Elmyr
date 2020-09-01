package fr.xgouchet.elmyr.inject.dummy

import fr.xgouchet.elmyr.annotation.IntForgery

open class KotlinInjectedUnknownPrimitiveGeneric {

    @IntForgery
    var comparatorInt: Comparator<Int>? = null
}
