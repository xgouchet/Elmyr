package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.util.TimeZone

class TimeZoneForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery timeZone1: TimeZone,
        @Forgery timeZone2: TimeZone
    ) {
        assertThat(timeZone1.toZoneId().toString())
                .isNotEqualTo(timeZone2.toZoneId().toString())
    }

    companion object {
        @RegisterExtension
        @JvmField
        val FORGE = ForgeExtension()
                .withFactory(TimeZoneForgeryFactory())

        const val ONE_YEAR_MILLIS: Long = 365L * 24L * 60L * 60L * 1000L
    }
}