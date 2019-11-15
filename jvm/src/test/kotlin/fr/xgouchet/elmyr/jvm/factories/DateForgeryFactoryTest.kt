package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeConfiguration
import fr.xgouchet.elmyr.junit5.ForgeExtension
import fr.xgouchet.elmyr.jvm.JvmConfigurator
import java.util.Date
import kotlin.math.abs
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(JvmConfigurator::class)
class DateForgeryFactoryTest {

    @Test
    fun `forge date within one year`(@Forgery fakeDate: Date) {
        val now = System.currentTimeMillis()

        val diff = abs(fakeDate.time - now)

        assertThat(diff)
                .isLessThanOrEqualTo(ONE_YEAR_MILLIS)
    }

    @Test
    fun `forges different values`(
        @Forgery date1: Date,
        @Forgery date2: Date
    ) {
        assertThat(date1.time)
                .isNotEqualTo(date2.time)
    }

    companion object {
        const val ONE_YEAR_MILLIS: Long = 365L * 24L * 60L * 60L * 1000L
    }
}
