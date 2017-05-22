package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.Case
import fr.xgouchet.elmyr.CharConstraint
import fr.xgouchet.elmyr.IntConstraint
import fr.xgouchet.elmyr.StringConstraint
import fr.xgouchet.elmyr.kotlin.ElmyrDelegates.forgery
import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Assertions.assertThat

/**
 * @author Xavier F. Gouchet
 */
class ElmyrDelegatesSpecs : FeatureSpec() {

    internal val forgedStringReadOnly: String by forgery(StringConstraint.ANY)
    internal val forgedStringRegex: String by forgery(Regex("""Hello \w+ !"""))
    internal val forgedStringConstraint: String by forgery(StringConstraint.HEXADECIMAL, Case.LOWER, SIZE)

    internal val forgedCharReadOnly: Char by forgery(CharConstraint.ALPHA_NUM, Case.UPPER)
    internal val forgedCharNumerical: Char by forgery(CharConstraint.NUMERICAL)

    internal val forgedIntReadOnly: Int by forgery(IntConstraint.TINY)
    internal val forgedIntWithRange: Int by forgery(SIZE, LIMIT)

    init {

        feature("String delegates") {

            scenario("Read Only property") {
                val s1 = forgedStringReadOnly
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