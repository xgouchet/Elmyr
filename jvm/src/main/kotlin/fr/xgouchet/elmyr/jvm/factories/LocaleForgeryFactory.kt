package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import java.util.Locale

class LocaleForgeryFactory
    : ForgeryFactory<Locale>{

    override fun getForgery(forge: Forge): Locale {
        val availableLocales = Locale.getAvailableLocales()
        return forge.anElementFrom(*availableLocales)
    }

}