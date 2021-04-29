package fr.xgouchet.elmyr.inject.fixture

import fr.xgouchet.elmyr.annotation.BoolForgery
import fr.xgouchet.elmyr.annotation.DoubleForgery
import fr.xgouchet.elmyr.annotation.FloatForgery
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.annotation.LongForgery

open class KotlinInjectedPrimitives {

    @BoolForgery
    var publicBool: Boolean = false
    @BoolForgery(probability = 0.01f)
    protected var protectedBool: Boolean = false
    @BoolForgery(probability = 0.99f)
    private var privateBool: Boolean = false
    @BoolForgery
    lateinit var publicBoolList: List<Boolean>
    @BoolForgery
    lateinit var publicBoolSet: List<Boolean>
    @BoolForgery
    lateinit var publicBoolCollection: List<Boolean>

    @IntForgery
    var publicInt: Int = 0
    @IntForgery(min = 3, max = 43)
    var publicRangeInt: Int = 0
    @IntForgery(mean = 1337, standardDeviation = 10)
    var publicGaussianInt: Int = 0
    @IntForgery
    protected var protectedInt: Int = 0
    @IntForgery
    internal var internalInt: Int = 0
    @IntForgery
    private var privateInt: Int = 0
    @IntForgery
    lateinit var publicIntList: List<Int>
    @IntForgery
    lateinit var publicIntSet: List<Int>
    @IntForgery
    lateinit var publicIntCollection: List<Int>

    @LongForgery
    var publicLong: Long = 0
    @LongForgery(min = 3, max = 43)
    var publicRangeLong: Long = 0
    @LongForgery(mean = 1337, standardDeviation = 10)
    var publicGaussianLong: Long = 0
    @LongForgery
    protected var protectedLong: Long = 0
    @LongForgery
    internal var internalLong: Long = 0
    @LongForgery
    private var privateLong: Long = 0
    @LongForgery
    lateinit var publicLongList: List<Long>
    @LongForgery
    lateinit var publicLongSet: List<Long>
    @LongForgery
    lateinit var publicLongCollection: List<Long>

    @FloatForgery
    var publicFloat: Float = 0f
    @FloatForgery(min = 3f, max = 43f)
    var publicRangeFloat: Float = 0f
    @FloatForgery(mean = 1337f, standardDeviation = 10f)
    var publicGaussianFloat: Float = 0f
    @FloatForgery
    protected var protectedFloat: Float = 0f
    @FloatForgery
    internal var internalFloat: Float = 0f
    @FloatForgery
    private var privateFloat: Float = 0f
    @FloatForgery
    lateinit var publicFloatList: List<Float>
    @FloatForgery
    lateinit var publicFloatSet: List<Float>
    @FloatForgery
    lateinit var publicFloatCollection: List<Float>

    @DoubleForgery
    var publicDouble: Double = 0.0
    @DoubleForgery(min = 3.0, max = 43.0)
    var publicRangeDouble: Double = 0.0
    @DoubleForgery(mean = 1337.0, standardDeviation = 10.0)
    var publicGaussianDouble: Double = 0.0
    @DoubleForgery
    protected var protectedDouble: Double = 0.0
    @DoubleForgery
    internal var internalDouble: Double = 0.0
    @DoubleForgery
    private var privateDouble: Double = 0.0
    @DoubleForgery
    lateinit var publicDoubleList: List<Double>
    @DoubleForgery
    lateinit var publicDoubleSet: List<Double>
    @DoubleForgery
    lateinit var publicDoubleCollection: List<Double>

    fun retrieveProtectedInt(): Int = protectedInt

    fun retrievePrivateInt(): Int {
        doSomething()
        return privateInt
    }

    fun retrieveProtectedLong(): Int = protectedInt

    fun retrievePrivateLong(): Long {
        doSomething()
        return privateLong
    }

    fun retrieveProtectedFloat(): Float = protectedFloat

    fun retrievePrivateFloat(): Float {
        doSomething()
        return privateFloat
    }

    fun retrieveProtectedDouble(): Double = protectedDouble

    fun retrievePrivateDouble(): Double {
        doSomething()
        return privateDouble
    }

    private fun doSomething() {
    }
}
