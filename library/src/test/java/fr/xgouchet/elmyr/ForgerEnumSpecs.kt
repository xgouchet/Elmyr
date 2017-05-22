package fr.xgouchet.elmyr

/**
 * @author Xavier F. Gouchet
 */
class ForgerEnumSpecs : io.kotlintest.specs.FeatureSpec() {

    enum class Month {
        JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC
    }

    enum class Day {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

    init {

        feature("An Enum Forger uses a seed") {

            val forger = fr.xgouchet.elmyr.Forger()
            val seed = System.nanoTime()
            forger.reset(seed)
            val selectedMonth = forger.aValueFrom(fr.xgouchet.elmyr.ForgerEnumSpecs.Month::class.java)
            val selectedDay = forger.aValueFrom(fr.xgouchet.elmyr.ForgerEnumSpecs.Day::class.java)

            scenario("Reproduce data with another forger using the same seed") {
                val otherProvider = fr.xgouchet.elmyr.Forger()
                otherProvider.reset(seed)

                val otherSelectedMonth = otherProvider.aValueFrom(fr.xgouchet.elmyr.ForgerEnumSpecs.Month::class.java)
                val otherSelectedDay = otherProvider.aValueFrom(fr.xgouchet.elmyr.ForgerEnumSpecs.Day::class.java)

                org.assertj.core.api.Assertions.assertThat(otherSelectedMonth)
                        .isEqualTo(selectedMonth)
                org.assertj.core.api.Assertions.assertThat(otherSelectedDay)
                        .isEqualTo(selectedDay)
            }

            scenario("Reproduce data with same forger reset with the same seed") {
                forger.reset(seed)
                val otherSelectedMonth = forger.aValueFrom(fr.xgouchet.elmyr.ForgerEnumSpecs.Month::class.java)
                val otherSelectedDay = forger.aValueFrom(fr.xgouchet.elmyr.ForgerEnumSpecs.Day::class.java)

                org.assertj.core.api.Assertions.assertThat(otherSelectedMonth)
                        .isEqualTo(selectedMonth)
                org.assertj.core.api.Assertions.assertThat(otherSelectedDay)
                        .isEqualTo(selectedDay)
            }

        }
    }
}

