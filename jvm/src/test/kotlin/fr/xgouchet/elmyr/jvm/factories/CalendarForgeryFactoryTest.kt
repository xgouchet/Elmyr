package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.util.Calendar
import kotlin.math.abs

class CalendarForgeryFactoryTest {

    @Test
    fun `forge calendar within one year`(@Forgery fakeCalendar: Calendar) {
        val now = System.currentTimeMillis()

        val diff = abs(fakeCalendar.timeInMillis - now)

        assertThat(diff)
                .isLessThanOrEqualTo(ONE_YEAR_MILLIS)
    }

    @Test
    fun `forges different values`(
        @Forgery cal1: Calendar,
        @Forgery cal2: Calendar
    ) {
        assertThat(cal1.timeInMillis)
                .isNotEqualTo(cal2.timeInMillis)
    }

    companion object {
        @RegisterExtension
        @JvmField
        val FORGE = ForgeExtension()
                .withFactory(CalendarForgeryFactory())

        const val ONE_YEAR_MILLIS: Long = 365L * 24L * 60L * 60L * 1000L
    }
}