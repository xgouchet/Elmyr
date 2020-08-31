package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeConfiguration
import fr.xgouchet.elmyr.junit5.ForgeExtension
import fr.xgouchet.elmyr.jvm.JvmConfigurator
import java.util.TimeZone
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(JvmConfigurator::class)
class TimeZoneForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery timeZone1: TimeZone,
        @Forgery timeZone2: TimeZone,
        @Forgery timeZone3: TimeZone,
        @Forgery timeZone4: TimeZone
    ) {
        val distinctValues = setOf(
            timeZone1.toZoneId().toString(),
            timeZone2.toZoneId().toString(),
            timeZone3.toZoneId().toString(),
            timeZone4.toZoneId().toString()
        )
        assertThat(distinctValues.size)
            .isGreaterThan(1)
    }
}
