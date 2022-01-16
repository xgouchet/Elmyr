package fr.xgouchet.elmyr.junit4

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.annotation.Forgery
import org.junit.rules.MethodRule
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement

/**
 * A JUnit rule to keep your test class clean and automate the creation of fake data.
 *
 * It provides a [Forge] with the seed reset on each test, and will inject forgery on fields/properties
 * annotated with [Forgery].
 *
 * In case of failure, the seed of the [Forge] is printed to the [System.err] output stream, allowing
 * you to reproduce consistently failing tests.
 *
 * @property ruleSeed the seed to reset the [Forge] for each test
 *
 */
class ForgeRule(
    private val ruleSeed: Long = 0L
) :
        Forge(),
        MethodRule {

    // region Java/Factory

    /**
     * Adds a factory to the forge. This is the best way to extend a forge and provides means to
     * create custom forgeries.
     * @param T the type the [ForgeryFactory] will be able to forge
     * @param forgeryFactory the factory to be used
     * @return the same [ForgeRule] instance, perfect to chain calls
     */
    inline fun <reified T : Any> withFactory(forgeryFactory: ForgeryFactory<T>): ForgeRule {
        addFactory(T::class.java, forgeryFactory)
        return this
    }

    /**
     * Adds a factory to the forge. This is the best way to extend a forge and provides means to
     * create custom forgeries.
     * @param T the type the [ForgeryFactory] will be able to forge
     * @param clazz the class of type T
     * @param forgeryFactory the factory to be used
     * @return the same [ForgeRule] instance, perfect to chain calls
     */
    fun <T : Any> withFactory(clazz: Class<T>, forgeryFactory: ForgeryFactory<T>): ForgeRule {
        addFactory(clazz, forgeryFactory)
        return this
    }

    // endregion

    // region MethodRule

    /** @inheritdoc */
    override fun apply(base: Statement, method: FrameworkMethod, target: Any): Statement {
        resetSeed()
        return ForgeStatement(base, method, target, this)
    }

    // endregion

    // region Internal

    private fun resetSeed() {
        seed = if (ruleSeed == 0L) seed() else ruleSeed
    }

    // endregion
}
