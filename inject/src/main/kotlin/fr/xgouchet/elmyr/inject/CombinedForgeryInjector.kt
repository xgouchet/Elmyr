package fr.xgouchet.elmyr.inject

import fr.xgouchet.elmyr.Forge

/**
 * A [ForgeryInjector] implementation able to deal with both Java and Kotlin classes.
 */
class CombinedForgeryInjector : ForgeryInjector {

    private val delegates = listOf(
            JavaForgeryInjector(),
            KotlinForgeryInjector()
    )

    /** @inheritdoc */
    override fun inject(forge: Forge, target: Any) {
        delegates.forEach { it.inject(forge, target) }
    }
}