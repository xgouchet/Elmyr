package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.util.UUID

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

    companion object {
        @RegisterExtension
        @JvmField
        val FORGE = ForgeExtension()
                .withFactory(UUIDForgeryFactory())

        const val ONE_YEAR_MILLIS: Long = 365L * 24L * 60L * 60L * 1000L
    }
}