package fr.xgouchet.elmyr.junit

import org.junit.AssumptionViolatedException
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.MultipleFailureException
import org.junit.runners.model.Statement

/**
 * @author Xavier F. Gouchet
 */
class Repeater : TestRule {

    @Suppress("TooGenericExceptionCaught")
    override fun apply(base: Statement, description: Description): Statement {
        return RepeaterStatement(base, description)
    }

    class RepeaterStatement(private val base: Statement,
                            private val description: Description)
        : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {

            val repeat = description.getAnnotation(Repeat::class.java)
            val count = repeat?.count ?: 1
            val failureThreshold = repeat?.failureThreshold ?: 0
            val ignoreThreshold = repeat?.ignoreThreshold ?: 0

            val (errors, ignores) = evaluateRepeated(count)

            if (errors.size > failureThreshold) {
                throw MultipleFailureException(errors)
            } else if (ignores.size > ignoreThreshold) {
                val msg = "${ignores.size}(over ${repeat.count}) iteration(s) were ignored for ${description.displayName}"
                throw AssumptionViolatedException(msg)
            }
        }

        @Suppress("TooGenericExceptionCaught")
        private fun evaluateRepeated(count: Int)
                : Pair<List<Throwable>, List<Throwable>> {
            val errors = mutableListOf<Throwable>()
            val ignores = mutableListOf<Throwable>()

            repeat(count) {
                try {
                    base.evaluate()
                } catch (e: AssumptionViolatedException) {
                    ignores.add(e)
                } catch (e: Throwable) {
                    errors.add(e)
                }
            }

            return Pair(errors, ignores)
        }
    }
}
