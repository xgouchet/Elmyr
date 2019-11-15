package fr.xgouchet.elmyr.jvm

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeConfiguration
import fr.xgouchet.elmyr.junit5.ForgeExtension
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.util.Calendar
import java.util.Currency
import java.util.Date
import java.util.Locale
import java.util.Random
import java.util.TimeZone
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(JvmConfigurator::class)
class JvmFactoryTest {

    @Test
    fun `forges a Calendar`(@Forgery calendar: Calendar) {
        assertThat(calendar)
                .isNotNull()
    }

    @Test
    fun `forges a Currency`(@Forgery currency: Currency) {
        assertThat(currency)
                .isNotNull()
    }

    @Test
    fun `forges a Date`(@Forgery date: Date) {
        assertThat(date)
                .isNotNull()
    }

    @Test
    fun `forges a Locale`(@Forgery locale: Locale) {
        assertThat(locale)
                .isNotNull()
    }

    @Test
    fun `forges a Random`(@Forgery random: Random) {
        assertThat(random)
                .isNotNull()
    }

    @Test
    fun `forges a TimeZone`(@Forgery timeZone: TimeZone) {
        assertThat(timeZone)
                .isNotNull()
    }

    @Test
    fun `forges a Uuid`(@Forgery uuid: Duration) {
        assertThat(uuid)
                .isNotNull()
    }

    @Test
    fun `forges a Uuid`(@Forgery uuid: Period) {
        assertThat(uuid)
                .isNotNull()
    }

    @Test
    fun `forges a Uuid`(@Forgery uuid: LocalTime) {
        assertThat(uuid)
                .isNotNull()
    }

    @Test
    fun `forges a Uuid`(@Forgery uuid: LocalDate) {
        assertThat(uuid)
                .isNotNull()
    }

    @Test
    fun `forges a Uuid`(@Forgery uuid: LocalDateTime) {
        assertThat(uuid)
                .isNotNull()
    }
}
