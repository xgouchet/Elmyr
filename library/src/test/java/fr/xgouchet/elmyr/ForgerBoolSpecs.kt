package fr.xgouchet.elmyr

import io.kotlintest.specs.FeatureSpec
import org.assertj.core.api.Java6Assertions
import org.assertj.core.api.Java6Assertions.assertThat
import org.assertj.core.api.Java6Assertions.within

/**
 * @author Xavier F. Gouchet
 */
class ForgerBoolSpecs : FeatureSpec() {

    init {
        feature("A Boolean Forger produces booleans") {

            val forger = Forger()
            forger.reset(System.nanoTime())

            scenario("Produce random boolean ") {
                val baseData = BooleanArray(16, { forger.aBool() })

                repeat(16, {
                    val otherData = BooleanArray(16, { forger.aBool() })
                    Java6Assertions.assertThat(baseData)
                            .isNotEqualTo(otherData)
                })
            }

            scenario("Produce random boolean with probability") {
                val count = 1024
                val probability = forger.aFloat(0f, 1f)

                var countTrue = 0f

                repeat(count, {
                    val b = forger.aBool(probability)
                    if (b) {
                        countTrue++
                    }
                })

                assertThat(countTrue/count)
                        .isCloseTo(probability, within(0.1f))
            }
        }
    }
}