package fr.xgouchet.elmyr.inject.dummy

import fr.xgouchet.elmyr.annotation.AdvancedForgery
import fr.xgouchet.elmyr.annotation.DoubleForgery
import fr.xgouchet.elmyr.annotation.FloatForgery
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.annotation.LongForgery
import fr.xgouchet.elmyr.annotation.StringForgery
import fr.xgouchet.elmyr.annotation.StringForgeryType

class KotlinInjectedAdvanced {

    @AdvancedForgery(
        string = [
            StringForgery(StringForgeryType.NUMERICAL),
            StringForgery(StringForgeryType.ALPHABETICAL)
        ]
    )
    lateinit var publicAlphaOrNumString: String

    @AdvancedForgery(
        int = [
            IntForgery(20, 30),
            IntForgery(100, 110)
        ]
    )
    var publicMultipleRangesInt: Int = 0

    @AdvancedForgery(
        int = [
            IntForgery(mean = -100, standardDeviation = 10),
            IntForgery(mean = 100, standardDeviation = 10)
        ]
    )
    var publicMultipleMeansInt: Int = 0

    @AdvancedForgery(
        long = [
            LongForgery(20L, 30L),
            LongForgery(100L, 110L)
        ]
    )
    var publicMultipleRangesLong: Long = 0

    @AdvancedForgery(
        long = [
            LongForgery(mean = -100L, standardDeviation = 10L),
            LongForgery(mean = 100L, standardDeviation = 10L)
        ]
    )
    var publicMultipleMeansLong: Long = 0

    @AdvancedForgery(
        float = [
            FloatForgery(20f, 30f),
            FloatForgery(100f, 110f)
        ]
    )
    var publicMultipleRangesFloat: Float = 0f

    @AdvancedForgery(
        float = [
            FloatForgery(mean = -100f, standardDeviation = 10f),
            FloatForgery(mean = 100f, standardDeviation = 10f)
        ]
    )
    var publicMultipleMeansFloat: Float = 0f

    @AdvancedForgery(
        double = [
            DoubleForgery(20.0, 30.0),
            DoubleForgery(100.0, 110.0)
        ]
    )
    var publicMultipleRangesDouble: Double = 0.0

    @AdvancedForgery(
        double = [
            DoubleForgery(mean = -100.0, standardDeviation = 10.0),
            DoubleForgery(mean = 100.0, standardDeviation = 10.0)
        ]
    )
    var publicMultipleMeansDouble: Double = 0.0
}
