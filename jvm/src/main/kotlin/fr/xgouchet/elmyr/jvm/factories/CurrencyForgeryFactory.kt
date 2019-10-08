package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import java.util.Currency

/**
 * A [ForgeryFactory] that will generate a [Currency] based on the available currencies on the
 * current system.
 */
class CurrencyForgeryFactory :
    ForgeryFactory<Currency> {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): Currency {
        val availableCurrencies = Currency.getAvailableCurrencies()
        return forge.anElementFrom(availableCurrencies)
    }
}