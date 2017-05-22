package fr.xgouchet.elmyr

import io.kotlintest.specs.FeatureSpec

/**
 * @author Xavier F. Gouchet
 */
class ForgerEnumSpecs : FeatureSpec() {

    enum class Month {
        JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC
    }

    enum class Day {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

    init {

        feature("An Enum Forger uses a seed") {

            val forger = Forger()
            val seed = System.nanoTime()
            forger.reset(seed)
            val selectedMonth = forger.aValueFrom(ForgerEnumSpecs.Month::class.java)
            val selectedDay = forger.aValueFrom(ForgerEnumSpecs.Day::class.java)

            scenario("Reproduce data with another forger using the same seed") {
                val otherProvider = Forger()
                otherProvider.reset(seed)

                val otherSelectedMonth = otherProvider.aValueFrom(ForgerEnumSpecs.Month::class.java)
                val otherSelectedDay = otherProvider.aValueFrom(ForgerEnumSpecs.Day::class.java)

                org.assertj.core.api.Assertions.assertThat(otherSelectedMonth)
                        .isEqualTo(selectedMonth)
                org.assertj.core.api.Assertions.assertThat(otherSelectedDay)
                        .isEqualTo(selectedDay)
            }

            scenario("Reproduce data with same forger reset with the same seed") {
                forger.reset(seed)
                val otherSelectedMonth = forger.aValueFrom(ForgerEnumSpecs.Month::class.java)
                val otherSelectedDay = forger.aValueFrom(ForgerEnumSpecs.Day::class.java)

                org.assertj.core.api.Assertions.assertThat(otherSelectedMonth)
                        .isEqualTo(selectedMonth)
                org.assertj.core.api.Assertions.assertThat(otherSelectedDay)
                        .isEqualTo(selectedDay)
            }

        }
    }
}

