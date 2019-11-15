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
        @Forgery timeZone2: TimeZone
    ) {
        assertThat(timeZone1.toZoneId().toString())
                .isNotEqualTo(timeZone2.toZoneId().toString())
    }
}
