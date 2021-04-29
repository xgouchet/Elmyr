package fr.xgouchet.elmyr.inject.fixture

import fr.xgouchet.elmyr.annotation.AdvancedForgery
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.annotation.LongForgery
import fr.xgouchet.elmyr.annotation.PairForgery
import fr.xgouchet.elmyr.annotation.StringForgery
import fr.xgouchet.elmyr.annotation.StringForgeryType

class KotlinInjectedPair {

    @PairForgery(
        first = AdvancedForgery(string = [StringForgery(StringForgeryType.HEXADECIMAL)]),
        second = AdvancedForgery(long = [LongForgery(10)])
    )
    lateinit var publicPair: Pair<String, Long>

    @PairForgery(
        first = AdvancedForgery(string = [StringForgery(regex = "\\w+@\\w+\\.[a-z]{3}")])
    )
    lateinit var publicFooPair: Pair<String, Foo>

    @PairForgery(
        first = AdvancedForgery(string = [StringForgery(StringForgeryType.NUMERICAL)]),
        second = AdvancedForgery(
            pair = [
                PairForgery(
                    first = AdvancedForgery(int = [IntForgery(10, 100)])
                )
            ]
        )
    )
    lateinit var publicNestedFooPair: Pair<String, Pair<Int, Foo>>
}
