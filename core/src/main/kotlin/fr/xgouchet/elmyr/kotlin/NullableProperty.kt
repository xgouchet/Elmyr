package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryAware
import kotlin.properties.ReadOnlyProperty

/**
 * This class implements a [ReadOnlyProperty], forging a random object with a given probability of being null.
 *
 * @param delegate the delegate that will forge a non-null random object of the desired type
 * @param probability the probability the instance will be null (default 0.5f)
 */
class NullableProperty<T : Any>(
    val delegate: ForgeryProperty<T>,
    val probability: Float = Forge.HALF_PROBABILITY
) : ForgeryProperty<T?>() {

    override fun getForgery(forge: Forge): T? {
        val isNull = forge.aBool(probability)
        return if (isNull) null else delegate.getForgery(forge)
    }
}

/**
 * Creates a [ReadOnlyProperty] that will forge a random object with a given probability of being null.
 *
 * @param delegate the delegate that will forge a non-null random object of the desired type
 * @param probability the probability the instance will be null (default 0.5f)
 */
fun <T : Any> nullableForgery(
    delegate: ForgeryProperty<T>,
    probability: Float = Forge.HALF_PROBABILITY
): ReadOnlyProperty<ForgeryAware, T?> = NullableProperty(delegate, probability)

/**
 * Creates a [ReadOnlyProperty] that will forge a random object with a given probability of being null.
 *
 * @param lambda the lambda that will forge a non-null random object of the desired type
 * @param probability the probability the instance will be null (default 0.5f)
 */
fun <T : Any> nullableForgery(
    probability: Float = Forge.HALF_PROBABILITY,
    lambda: Forge.() -> T
): ReadOnlyProperty<ForgeryAware, T?> = NullableProperty(LambdaProperty(lambda), probability)
