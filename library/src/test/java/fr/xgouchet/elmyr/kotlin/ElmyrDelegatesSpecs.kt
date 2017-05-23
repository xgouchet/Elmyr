package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.*
import fr.xgouchet.elmyr.kotlin.ElmyrDelegates.forgery
import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Assertions.assertThat

/**
 * @author Xavier F. Gouchet
 */
class ElmyrDelegatesSpecs : FeatureSpec() {

    internal val baseForger: Forger = Forger()

    internal val forgedStringReadOnly: String by forgery(StringConstraint.ANY, forger = baseForger)
    internal val forgedStringRegex: String by forgery(Regex("""Hello \w+ !"""), forger = baseForger)
    internal val forgedStringConstraint: String by forgery(StringConstraint.HEXADECIMAL, Case.LOWER, SIZE, forger = baseForger)

    internal val forgedCharReadOnly: Char by forgery(CharConstraint.ALPHA_NUM, Case.UPPER, forger = baseForger)
    internal val forgedCharNumerical: Char by forgery(CharConstraint.NUMERICAL, forger = baseForger)

    internal val forgedIntReadOnly: Int by forgery(IntConstraint.TINY, forger = baseForger)
    internal val forgedIntWithRange: Int by forgery(SIZE, LIMIT, forger = baseForger)

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
                        .isGreaterThanOrEqualTo(SIZE)
                        .isLessThan(LIMIT)
            }
        }
    }

    companion object {
        val SIZE: Int = 42
        val LIMIT: Int = 24601
    }
}