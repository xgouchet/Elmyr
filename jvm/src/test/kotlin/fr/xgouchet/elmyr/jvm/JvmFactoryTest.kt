package fr.xgouchet.elmyr.jvm

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.util.Calendar
import java.util.Currency
import java.util.Date
import java.util.Locale
import java.util.Random
import java.util.TimeZone
import java.util.UUID

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
    fun `forges a Uuid`(@Forgery uuid: UUID) {
        assertThat(uuid)
                .isNotNull()
    }

    companion object {
        @RegisterExtension
        @JvmField
        val FORGE = ForgeExtension().configure {
            useJvmFactories()
        }

        const val ONE_YEAR_MILLIS: Long = 365L * 24L * 60L * 60L * 1000L
    }
}