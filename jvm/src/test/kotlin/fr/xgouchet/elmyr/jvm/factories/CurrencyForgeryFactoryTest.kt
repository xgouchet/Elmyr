package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeConfiguration
import fr.xgouchet.elmyr.junit5.ForgeExtension
import fr.xgouchet.elmyr.jvm.JvmConfigurator
import java.util.Currency
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ForgeExtension::class)
@ForgeConfiguration(JvmConfigurator::class)
class CurrencyForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery cur1: Currency,
        @Forgery cur2: Currency
    ) {
        assertThat(cur1.currencyCode)
                .isNotEqualTo(cur2.currencyCode)
    }
}
