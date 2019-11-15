package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeConfiguration
import fr.xgouchet.elmyr.junit5.ForgeExtension
import fr.xgouchet.elmyr.jvm.JvmConfigurator
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(JvmConfigurator::class)
class UUIDForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery uuid1: UUID,
        @Forgery uuid2: UUID
    ) {
        assertThat(uuid1.leastSignificantBits)
                .isNotEqualTo(uuid2.leastSignificantBits)

        assertThat(uuid1.mostSignificantBits)
                .isNotEqualTo(uuid2.mostSignificantBits)
    }
}
