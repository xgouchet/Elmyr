package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.*
import fr.xgouchet.elmyr.kotlin.ElmyrDelegates.forgeryWithConstraint
import fr.xgouchet.elmyr.kotlin.ElmyrDelegates.forgeryWithDistribution
import fr.xgouchet.elmyr.kotlin.ElmyrDelegates.forgeryWithRange
import fr.xgouchet.elmyr.kotlin.ElmyrDelegates.forgeryWithRegex
import fr.xgouchet.elmyr.kotlin.ElmyrDelegates.nullable
import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.assertj.core.api.Java6Assertions
import kotlin.reflect.KProperty

/**
 * @author Xavier F. Gouchet
 */
class ElmyrDelegatesSpecs : FeatureSpec() {

    internal val baseForger: Forger = Forger()

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

    init {

        feature("String delegates") {

            scenario("Read Only property") {
                val s1 = forgedStringReadOnly
                val s2 = forgedStringReadOnly

                assertThat(s1).isEqualTo(s2)
            }

            scenario("Only use seed once") {
                val s1 = forgedStringReadOnly
                baseForger.reset(System.nanoTime())
                val s2 = forgedStringReadOnly

                assertThat(s1).isEqualTo(s2)
            }


            scenario("Use forger lazily") {
                val seed = System.nanoTime()
                baseForger.reset(seed)
                val s1 = baseForger.aString(StringConstraint.ANY, Case.ANY, -1)

                baseForger.reset(seed)
                val s2 = forgedStringReadOnly

                assertThat(s1).isEqualTo(s2)
            }

            scenario("With regex") {
                val s1 = forgedStringRegex
                val s2 = forgedStringRegex

                assertThat(s1).isEqualTo(s2)

                assertThat(s1).matches("Hello \\w+ !")
            }

            scenario("With regex pattern") {
                val s1 = forgedStringRegex2
                val s2 = forgedStringRegex2

                assertThat(s1).isEqualTo(s2)

                assertThat(s1).matches("Bye \\w+ !")
            }

            scenario("With constraint") {
                val s1 = forgedStringConstraint
                val s2 = forgedStringConstraint

                assertThat(s1).isEqualTo(s2)

                assertThat(s1).matches("[a-f0-9]{$SIZE}")
            }
        }

        feature("Char delegates") {

            scenario("Read Only property") {
                val s1 = forgedCharReadOnly
                val s2 = forgedCharReadOnly

                assertThat(s1).isEqualTo(s2)
            }

            scenario("numerical") {
                val s1 = forgedCharNumerical
                val s2 = forgedCharNumerical

                assertThat(s1).isEqualTo(s2)
                        .isIn('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
            }
        }

        feature("Int delegates") {

            scenario("Read Only property") {
                val s1 = forgedIntReadOnly
                val s2 = forgedIntReadOnly

                assertThat(s1).isEqualTo(s2)
            }

            scenario("numerical") {
                val s1 = forgedIntWithRange
                val s2 = forgedIntWithRange

                assertThat(s1).isEqualTo(s2)
                        .isGreaterThanOrEqualTo(MIN)
                        .isLessThan(MAX)
            }
        }

        feature("Float delegates") {

            scenario("Read Only property") {
                val s1 = forgedFloatReadOnly
                val s2 = forgedFloatReadOnly

                assertThat(s1).isEqualTo(s2)
            }

            scenario("Within range") {
                val s1 = forgedFloatWithRange
                val s2 = forgedFloatWithRange

                assertThat(s1).isEqualTo(s2)
                        .isGreaterThanOrEqualTo(MIN.toFloat())
                        .isLessThan(MAX.toFloat())
            }

            scenario("Gaussian probability") {
                val s1 = forgedFloatWithDistribution
                val s2 = forgedFloatWithDistribution

                assertThat(s1).isEqualTo(s2)
                        .isCloseTo(MEAN, within(ST_DEV * 10))
            }
        }

        feature("Nullable properties") {
            val probability = baseForger.aFloat(0f, 1f)
            var countNull = 0f
            val count = 1024

            repeat(count, {
                val obj = ObjectWithNullableProperty(baseForger, probability)
                val p1 = obj.forgedNullableString
                val p2 = obj.forgedNullableString

                assertThat(p1).isEqualTo(p2)

                if (p1 == null) {
                    countNull++
                }

            })


            assertThat(countNull / count)
                    .isCloseTo(probability, within(0.1f))
        }
    }

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