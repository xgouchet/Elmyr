package fr.xgouchet.elmyr.inject.dummy

import fr.xgouchet.elmyr.annotation.BoolForgery
import fr.xgouchet.elmyr.annotation.DoubleForgery
import fr.xgouchet.elmyr.annotation.FloatForgery
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.annotation.LongForgery

open class KotlinInjectedInvalidPrimitives {

    @BoolForgery(probability = 2f)
    var highProbabilityBool: Boolean = false
    @BoolForgery(probability = -1f)
    var negativeProbabilityBool: Boolean = false

    @IntForgery(min = 0, standardDeviation = 0)
    var invalidInt1: Int = 0
    @IntForgery(max = 0, standardDeviation = 0)
    var invalidInt2: Int = 0
    @IntForgery(min = 0, mean = 10)
    var invalidInt3: Int = 0
    @IntForgery(max = 0, mean = 10)
    var invalidInt4: Int = 0
    @IntForgery(min = 10, max = 0)
    var invalidInt5: Int = 0

    @LongForgery(min = 0, standardDeviation = 0)
    var invalidLong1: Long = 0
    @LongForgery(max = 0, standardDeviation = 0)
    var invalidLong2: Long = 0
    @LongForgery(min = 0, mean = 10)
    var invalidLong3: Long = 0
    @LongForgery(max = 0, mean = 10)
    var invalidLong4: Long = 0
    @LongForgery(min = 10, max = 0)
    var invalidLong5: Long = 0

    @FloatForgery(min = 0f, standardDeviation = 0f)
    var invalidFloat1: Float = 0f
    @FloatForgery(max = 0f, standardDeviation = 0f)
    var invalidFloat2: Float = 0f
    @FloatForgery(min = 0f, mean = 10f)
    var invalidFloat3: Float = 0f
    @FloatForgery(max = 0f, mean = 10f)
    var invalidFloat4: Float = 0f
    @FloatForgery(min = 10f, max = 0f)
    var invalidFloat5: Float = 0f

    @DoubleForgery(min = 0.0, standardDeviation = 0.0)
    var invalidDouble1: Double = 0.0
    @DoubleForgery(max = 0.0, standardDeviation = 0.0)
    var invalidDouble2: Double = 0.0
    @DoubleForgery(min = 0.0, mean = 10.0)
    var invalidDouble3: Double = 0.0
    @DoubleForgery(max = 0.0, mean = 10.0)
    var invalidDouble4: Double = 0.0
    @DoubleForgery(min = 10.0, max = 0.0)
    var invalidDouble5: Double = 0.0
}
