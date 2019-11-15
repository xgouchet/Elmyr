package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeConfiguration
import fr.xgouchet.elmyr.junit5.ForgeExtension
import fr.xgouchet.elmyr.jvm.JvmConfigurator
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.abs
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(JvmConfigurator::class)
class LocalDateTimeForgeryFactoryTest {

    @Test
    fun `forge date within one year`(@Forgery fakeDate: LocalDateTime) {
        val now = LocalDateTime.now()
        val zoneOffset = ZoneOffset.UTC

        val diff = abs(fakeDate.toEpochSecond(zoneOffset) - now.toEpochSecond(zoneOffset))

        assertThat(diff)
                .isLessThanOrEqualTo(ONE_YEAR_SEC)
    }

    @Test
    fun `forges different values`(
        @Forgery date1: LocalDateTime,
        @Forgery date2: LocalDateTime
    ) {
        assertThat(date1.compareTo(date2)).isNotEqualTo(0)
    }

    companion object {
        private const val ONE_YEAR_SEC: Long = 365L * 24L * 60L * 60L
    }
}
