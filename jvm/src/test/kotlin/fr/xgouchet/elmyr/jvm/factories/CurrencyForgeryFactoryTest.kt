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
        @Forgery currency1: Currency,
        @Forgery currency2: Currency,
        @Forgery currency3: Currency,
        @Forgery currency4: Currency
    ) {
        val distinctValues = setOf(
            currency1.currencyCode,
            currency2.currencyCode,
            currency3.currencyCode,
            currency4.currencyCode
        )
        assertThat(distinctValues.size)
            .isGreaterThan(1)
    }
}
