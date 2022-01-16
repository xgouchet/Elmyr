package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Forge
import kotlin.properties.ReadOnlyProperty

/**
 * This class implements a [ReadOnlyProperty], forging a random object with a given lambda.
 *
 * @param T the type of the element to forge
 * @property lambda the lambda that will forge a non-null random object of the desired type
 */
class LambdaProperty<T : Any>(
    private val lambda: Forge.() -> T
) : ForgeryProperty<T>() {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): T {
        return forge.lambda()
    }
}
