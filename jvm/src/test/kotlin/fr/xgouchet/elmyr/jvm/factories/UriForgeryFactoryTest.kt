package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeConfiguration
import fr.xgouchet.elmyr.junit5.ForgeExtension
import fr.xgouchet.elmyr.jvm.JvmConfigurator
import java.net.URI
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(JvmConfigurator::class)
class UriForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery uri1: URI,
        @Forgery uri2: URI
    ) {
        assertThat(uri1.toString())
                .isNotEqualTo(uri2.toString())
    }

    @Test
    fun `forge many URIs`(@Forgery uris: List<URI>) {
        // Do nothing
    }
}
