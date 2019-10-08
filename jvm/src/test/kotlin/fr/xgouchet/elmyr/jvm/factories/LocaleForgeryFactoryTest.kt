package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.util.Locale

class LocaleForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery locale1: Locale,
        @Forgery locale2: Locale
    ) {
        assertThat(locale1.toString())
                .isNotEqualTo(locale2.toString())
    }

    companion object {
        @RegisterExtension
        @JvmField
        val FORGE = ForgeExtension()
                .withFactory(LocaleForgeryFactory())

        const val ONE_YEAR_MILLIS: Long = 365L * 24L * 60L * 60L * 1000L
    }
}