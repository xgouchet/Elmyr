package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeConfiguration
import fr.xgouchet.elmyr.junit5.ForgeExtension
import fr.xgouchet.elmyr.jvm.JvmConfigurator
import java.util.Locale
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(JvmConfigurator::class)
class LocaleForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery locale1: Locale,
        @Forgery locale2: Locale,
        @Forgery locale3: Locale,
        @Forgery locale4: Locale
    ) {
        val distinctValues = setOf(
            locale1.toString(),
            locale2.toString(),
            locale3.toString(),
            locale4.toString()
        )
        assertThat(distinctValues.size)
                .isGreaterThan(1)
    }
}
