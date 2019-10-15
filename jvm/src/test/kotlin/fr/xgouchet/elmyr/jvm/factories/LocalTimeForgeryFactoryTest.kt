package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.time.LocalTime

class LocalTimeForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery time1: LocalTime,
        @Forgery time2: LocalTime
    ) {
        assertThat(time1.compareTo(time2)).isNotEqualTo(0)
    }

    companion object {
        @RegisterExtension
        @JvmField
        val FORGE = ForgeExtension()
                .withFactory(LocalTimeForgeryFactory())
    }
}