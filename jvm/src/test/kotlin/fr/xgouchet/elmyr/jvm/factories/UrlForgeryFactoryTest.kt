package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeConfiguration
import fr.xgouchet.elmyr.junit5.ForgeExtension
import fr.xgouchet.elmyr.jvm.JvmConfigurator
import java.net.URL
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(JvmConfigurator::class)
class UrlForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery url1: URL,
        @Forgery url2: URL
    ) {
        assertThat(url1.toString())
                .isNotEqualTo(url2.toString())
    }

    @Test
    fun `forge many URLs`(@Forgery urls: List<URL>) {
        // Do nothing
    }
}
