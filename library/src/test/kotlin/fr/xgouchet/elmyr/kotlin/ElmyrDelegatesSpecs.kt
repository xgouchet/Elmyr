package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Case
import fr.xgouchet.elmyr.CharConstraint
import fr.xgouchet.elmyr.FloatConstraint
import fr.xgouchet.elmyr.Forger
import fr.xgouchet.elmyr.IntConstraint
import fr.xgouchet.elmyr.StringConstraint
import fr.xgouchet.elmyr.kotlin.ElmyrDelegates.forgeryWithConstraint
import fr.xgouchet.elmyr.kotlin.ElmyrDelegates.forgeryWithDistribution
import fr.xgouchet.elmyr.kotlin.ElmyrDelegates.forgeryWithRange
import fr.xgouchet.elmyr.kotlin.ElmyrDelegates.forgeryWithRegex
import fr.xgouchet.elmyr.kotlin.ElmyrDelegates.nullable
import fr.xgouchet.elmyr.kotlin.ObjectWithProperties.Companion.MAX
import fr.xgouchet.elmyr.kotlin.ObjectWithProperties.Companion.MEAN
import fr.xgouchet.elmyr.kotlin.ObjectWithProperties.Companion.MIN
import fr.xgouchet.elmyr.kotlin.ObjectWithProperties.Companion.SIZE
import fr.xgouchet.elmyr.kotlin.ObjectWithProperties.Companion.ST_DEV
import fr.xgouchet.elmyr.verifyProbability
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

/**
 * @author Xavier F. Gouchet
 */
class ElmyrDelegatesSpecs : Spek({

    describe("Read Only Delegates ") {
        val forger = Forger()
        var seed: Long = 0
        val testRepeatCountHuge = 1024
        var obj = ObjectWithProperties(forger)

        beforeEachTest {
            seed = System.nanoTime()
            forger.reset(seed)
            obj = ObjectWithProperties(forger)
        }

        context("String delegates") {

            it("Read Only property") {
                val s1 = obj.forgedStringReadOnly
                val s2 = obj.forgedStringReadOnly

                assertThat(s1).isEqualTo(s2)
            }

            it("Only use seed once") {
                val s1 = obj.forgedStringReadOnly
                forger.reset(System.nanoTime())
                val s2 = obj.forgedStringReadOnly

                assertThat(s1).isEqualTo(s2)
            }

            it("Use forger lazily") {
                val s2 = obj.forgedStringReadOnly

                forger.reset(seed)
                val s1 = forger.aString(StringConstraint.ANY)

                assertThat(s1).isEqualTo(s2)
            }

            it("With regex") {
                val s1 = obj.forgedStringRegex
                val s2 = obj.forgedStringRegex

                assertThat(s1).isEqualTo(s2)

                assertThat(s1).matches("Hello \\w+ !")
            }

            it("With regex pattern") {
                val s1 = obj.forgedStringRegex2
                val s2 = obj.forgedStringRegex2

                assertThat(s1).isEqualTo(s2)

                assertThat(s1).matches("Bye \\w+ !")
            }

            it("With constraint") {
                val s1 = obj.forgedStringConstraint
                val s2 = obj.forgedStringConstraint

                assertThat(s1).isEqualTo(s2)

                assertThat(s1).matches("[a-f0-9]{$SIZE}")
            }
        }

        context("Char delegates") {

            val obj = ObjectWithProperties(forger)

            it("Read Only property") {
                val s1 = obj.forgedCharReadOnly
                val s2 = obj.forgedCharReadOnly

                assertThat(s1).isEqualTo(s2)
            }

            it("numerical") {
                val s1 = obj.forgedCharNumerical
                val s2 = obj.forgedCharNumerical

                assertThat(s1).isEqualTo(s2)
                        .isIn('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
            }
        }

        context("Int delegates") {

            val obj = ObjectWithProperties(forger)

            it("Read Only property") {
                val s1 = obj.forgedIntReadOnly
                val s2 = obj.forgedIntReadOnly

                assertThat(s1).isEqualTo(s2)
            }

            it("numerical") {
                val s1 = obj.forgedIntWithRange
                val s2 = obj.forgedIntWithRange

                assertThat(s1).isEqualTo(s2)
                        .isGreaterThanOrEqualTo(MIN)
                        .isLessThan(MAX)
            }
        }

        context("Float delegates") {

            val obj = ObjectWithProperties(forger)

            it("Read Only property") {
                val s1 = obj.forgedFloatReadOnly
                val s2 = obj.forgedFloatReadOnly

                assertThat(s1).isEqualTo(s2)
            }

            it("Within range") {
                val s1 = obj.forgedFloatWithRange
                val s2 = obj.forgedFloatWithRange

                assertThat(s1).isEqualTo(s2)
                        .isGreaterThanOrEqualTo(MIN.toFloat())
                        .isLessThan(MAX.toFloat())
            }

            it("Gaussian probability") {
                val s1 = obj.forgedFloatWithDistribution
                val s2 = obj.forgedFloatWithDistribution

                assertThat(s1).isEqualTo(s2)
                        .isCloseTo(MEAN, within(ST_DEV * 10))
            }
        }

        context("Nullable properties") {
            val probability = forger.aFloat(0f, 1f)

            verifyProbability(testRepeatCountHuge,
                    probability.toDouble(),
                    { ->
                        val temp = ObjectWithNullableProperty(forger, probability)
                        temp.forgedNullableString == null
                    })
        }
    }
})

class ObjectWithProperties(baseForger: Forger) {

    internal val forgedStringReadOnly: String by forgeryWithConstraint(StringConstraint.ANY, forger = baseForger)
    internal val forgedStringRegex: String by forgeryWithRegex(Regex("""Hello \w+ !"""), forger = baseForger)
    internal val forgedStringRegex2: String by forgeryWithRegex("Bye \\w+ !", forger = baseForger)
    internal val forgedStringConstraint: String by forgeryWithConstraint(StringConstraint.HEXADECIMAL, Case.LOWER, SIZE, forger = baseForger)

    internal val forgedCharReadOnly: Char by forgeryWithConstraint(CharConstraint.ALPHA_NUM, Case.UPPER, forger = baseForger)
    internal val forgedCharNumerical: Char by forgeryWithConstraint(CharConstraint.NUMERICAL, forger = baseForger)

    internal val forgedIntReadOnly: Int by forgeryWithConstraint(IntConstraint.TINY, forger = baseForger)
    internal val forgedIntWithRange: Int by forgeryWithRange(MIN, MAX, forger = baseForger)

    internal val forgedFloatReadOnly: Float by forgeryWithConstraint(FloatConstraint.ANY, forger = baseForger)
    internal val forgedFloatWithRange: Float by forgeryWithRange(MIN.toFloat(), MAX.toFloat(), forger = baseForger)
    internal val forgedFloatWithDistribution: Float by forgeryWithDistribution(MEAN, ST_DEV, forger = baseForger)

    companion object {
        val SIZE: Int = 42

        val MIN: Int = 4815
        val MAX: Int = 24601

        val MEAN: Float = 69f
        val ST_DEV: Float = 3.141592f
    }
}

class ObjectWithNullableProperty(forger: Forger, probability: Float) {

    internal val forgedNullableString: String? by nullable(forgeryWithConstraint(StringConstraint.ANY, forger = forger), probability, forger)
}
