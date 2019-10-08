package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import java.util.Currency

class CurrencyForgeryFactory
    : ForgeryFactory<Currency> {
    override fun getForgery(forge: Forge): Currency {
        val availableCurrencies = Currency.getAvailableCurrencies()
        return forge.anElementFrom(availableCurrencies)
    }
}