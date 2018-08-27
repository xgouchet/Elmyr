package fr.xgouchet.elmyr.junit

import fr.xgouchet.elmyr.Forger
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.Locale

/**
 * @author Xavier F. Gouchet
 */
class JUnitForger :
        Forger(),
        TestRule {

    override fun apply(base: Statement, description: Description): Statement? {
        return InternalWatcher(this).apply(base, description)
    }

    private class InternalWatcher(private val forger: JUnitForger) : TestWatcher() {

        override fun starting(description: Description) {
            forger.reset()
        }

        override fun failed(e: Throwable, description: Description) {
            val message = "‘%s’ failed with fake seed = 0x%x"
            System.err.println(message.format(Locale.US, description.methodName, forger.seed))
        }
    }

    private fun reset() {
        val seed = (System.nanoTime() and SEED_MASK)
        reset(seed)
    }

    companion object {
        const val SEED_MASK = 0x7FFFFFFFL
    }
}
