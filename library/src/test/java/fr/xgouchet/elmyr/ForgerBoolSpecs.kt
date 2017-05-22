package fr.xgouchet.elmyr

import fr.xgouchet.elmyr.Forger
import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Java6Assertions

/**
 * @author Xavier F. Gouchet
 */
class ForgerBoolSpecs : FeatureSpec() {

    init {
        feature("A Boolean Forger produces booleans") {

            val forger = Forger()
            forger.reset(System.nanoTime())

            scenario("Produce an int in a specified range") {
                val baseData = BooleanArray(16, { forger.aBool() })

                repeat(16, {
                    val otherData = BooleanArray(16, { forger.aBool() })
                    Java6Assertions.assertThat(baseData)
                            .isNotEqualTo(otherData)
                })
            }
        }
    }
}