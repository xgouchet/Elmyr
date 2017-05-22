package fr.xgouchet.elmyr.kotlin

import fr.xgouchet.elmyr.kotlin.ElmyrDelegates.forgedString
import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Assertions.assertThat

/**
 * @author Xavier F. Gouchet
 */
class ElmyrDelegatesSpecs : FeatureSpec() {

    internal val forgedStringReadOnly : String by forgedString()
    internal val forgedStringRegex : String by forgedString(Regex("""Hello \w+ !"""))

    init {

        feature("String delegates") {

            scenario("Read Only property") {
                val s1 = forgedStringReadOnly
                val s2 =forgedStringReadOnly

                assertThat(s1).isEqualTo(s2)
            }

            scenario("With regex") {
                val s1 = forgedStringRegex
                val s2 =forgedStringRegex

                assertThat(s1).isEqualTo(s2)

                assertThat(s1).matches("Hello \\w+ !")
            }
        }
    }
}