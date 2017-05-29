package fr.xgouchet.elmyr.junit

import org.junit.AssumptionViolatedException
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.MultipleFailureException
import org.junit.runners.model.Statement
import java.util.*

/**
 * @author Xavier F. Gouchet
 */
class Repeater(val defaultCount: Int) : TestRule {

    constructor() : this(1)

    companion object {
        val PER_METHOD_COUNTER_REGEX = Regex(""".*_x(\d+)""")
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {

            @Throws(Throwable::class)
            override fun evaluate() {
                val errors = ArrayList<Throwable>()
                val ignores = ArrayList<Throwable>()

                val repeatCount: Int
                val match = PER_METHOD_COUNTER_REGEX.matchEntire(description.methodName)
                if (match == null) {
                    repeatCount = defaultCount
                } else {
                    repeatCount = match.groupValues[1].toInt()
                }

                repeat(repeatCount, {
                    try {
                        base.evaluate()
                    } catch (e: AssumptionViolatedException) {
                        ignores.add(e)
                    } catch (e: Throwable) {
                        errors.add(e)
                    } finally {
                    }
                })

                if (errors.isNotEmpty()) {
                    throw MultipleFailureException(errors)
                } else if (ignores.isNotEmpty()) {
                    if (ignores.size == defaultCount) {
                        throw AssumptionViolatedException("All iterations were ignored for ${description.displayName}")
                    } else {
                        throw AssumptionViolatedException("${ignores.size} iteration(s) were ignored for ${description.displayName}")
                    }
                }
            }
        }
    }


}