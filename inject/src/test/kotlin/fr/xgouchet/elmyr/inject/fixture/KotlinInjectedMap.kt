package fr.xgouchet.elmyr.inject.fixture

import fr.xgouchet.elmyr.annotation.AdvancedForgery
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.annotation.LongForgery
import fr.xgouchet.elmyr.annotation.MapForgery
import fr.xgouchet.elmyr.annotation.StringForgery
import fr.xgouchet.elmyr.annotation.StringForgeryType

class KotlinInjectedMap {

    @MapForgery(
        key = AdvancedForgery(string = [StringForgery(StringForgeryType.HEXADECIMAL)]),
        value = AdvancedForgery(long = [LongForgery(10)])
    )
    lateinit var publicMap: Map<String, Long>

    @MapForgery(
        key = AdvancedForgery(string = [StringForgery(regex = "\\w+@\\w+\\.[a-z]{3}")])
    )
    lateinit var publicFooMap: Map<String, Foo>

    @MapForgery(
        key = AdvancedForgery(string = [StringForgery(StringForgeryType.NUMERICAL)]),
        value = AdvancedForgery(
            map = [
                MapForgery(
                    key = AdvancedForgery(int = [IntForgery(10, 100)])
                )
            ]
        )
    )
    lateinit var publicNestedFooMap: Map<String, Map<Int, Foo>>
}
