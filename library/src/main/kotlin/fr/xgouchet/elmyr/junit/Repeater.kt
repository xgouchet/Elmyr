package fr.xgouchet.elmyr.junit

import org.junit.AssumptionViolatedException
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.MultipleFailureException
import org.junit.runners.model.Statement
import java.util.ArrayList

/**
 * @author Xavier F. Gouchet
 */
class Repeater : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {

            @Throws(Throwable::class)
            override fun evaluate() {
                val errors = ArrayList<Throwable>()
                val ignores = ArrayList<Throwable>()

                val repeat = description.getAnnotation(Repeat::class.java)
                val count = repeat?.count ?: 1

                repeat(count, {
                    try {
                        base.evaluate()
                    } catch (e: AssumptionViolatedException) {
                        ignores.add(e)
                    } catch (e: Throwable) {
                        errors.add(e)
                    } finally {
                    }
                })

                val failureThreshold = repeat?.failureThreshold ?: 0
                val ignoreThreshold = repeat?.ignoreThreshold ?: 0

                if (errors.size > failureThreshold) {
                    throw MultipleFailureException(errors)
                } else {
                    if (ignores.size > ignoreThreshold) {
                        throw AssumptionViolatedException("${ignores.size}(over ${repeat.count}) iteration(s) were ignored for ${description.displayName}")
                    }
                }
            }
        }
    }

}