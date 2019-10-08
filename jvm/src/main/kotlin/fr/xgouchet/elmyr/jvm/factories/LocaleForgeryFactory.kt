package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import java.util.Locale

/**
 * A [ForgeryFactory] that will generate a [Locale] based on the available locales on the current
 * system.
 */
class LocaleForgeryFactory :
    ForgeryFactory<Locale> {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): Locale {
        val availableLocales = Locale.getAvailableLocales()
        return forge.anElementFrom(*availableLocales)
    }
}