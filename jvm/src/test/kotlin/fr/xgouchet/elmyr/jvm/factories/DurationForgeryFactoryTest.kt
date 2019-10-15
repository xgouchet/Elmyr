package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.time.Duration

class DurationForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery duration1: Duration,
        @Forgery duration2: Duration
    ) {
        assertThat(duration1)
                .isNotEqualTo(duration2)
    }

    companion object {
        @RegisterExtension
        @JvmField
        val FORGE = ForgeExtension()
                .withFactory(DurationForgeryFactory())
    }
}