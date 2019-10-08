package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.ForgeExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.util.Currency

class CurrencyForgeryFactoryTest {

    @Test
    fun `forges different values`(
        @Forgery cur1: Currency,
        @Forgery cur2: Currency
    ) {
        assertThat(cur1.currencyCode)
                .isNotEqualTo(cur2.currencyCode)
    }

    companion object {
        @RegisterExtension
        @JvmField
        val FORGE = ForgeExtension()
                .withFactory(CurrencyForgeryFactory())

        const val ONE_YEAR_MILLIS: Long = 365L * 24L * 60L * 60L * 1000L
    }
}