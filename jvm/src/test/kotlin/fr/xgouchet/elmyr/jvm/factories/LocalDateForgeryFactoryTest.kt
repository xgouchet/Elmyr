package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeConfiguration
import fr.xgouchet.elmyr.junit5.ForgeExtension
import fr.xgouchet.elmyr.jvm.JvmConfigurator
import java.time.LocalDate
import kotlin.math.abs
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(JvmConfigurator::class)
class LocalDateForgeryFactoryTest {

    @Test
    fun `forge date within one year`(@Forgery fakeDate: LocalDate) {
        val now = LocalDate.now()

        val diff = abs(fakeDate.toEpochDay() - now.toEpochDay())

        assertThat(diff)
                .isLessThanOrEqualTo(ONE_YEAR_DAY)
    }

    @Test
    fun `forges different values`(
        @Forgery date1: LocalDate,
        @Forgery date2: LocalDate
    ) {
        assertThat(date1.compareTo(date2)).isNotEqualTo(0)
    }

    companion object {

        private const val ONE_YEAR_DAY: Long = 365L
    }
}
