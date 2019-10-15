package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.time.Period

class PeriodForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery period1: Period,
        @Forgery period2: Period
    ) {
        assertThat(period1)
                .isNotEqualTo(period2)
    }

    companion object {
        @RegisterExtension
        @JvmField
        val FORGE = ForgeExtension()
                .withFactory(PeriodForgeryFactory())
    }
}