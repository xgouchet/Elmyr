package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeConfiguration
import fr.xgouchet.elmyr.junit5.ForgeExtension
import fr.xgouchet.elmyr.jvm.JvmConfigurator
import java.time.LocalTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(JvmConfigurator::class)
class LocalTimeForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery time1: LocalTime,
        @Forgery time2: LocalTime
    ) {
        assertThat(time1.compareTo(time2)).isNotEqualTo(0)
    }
}
