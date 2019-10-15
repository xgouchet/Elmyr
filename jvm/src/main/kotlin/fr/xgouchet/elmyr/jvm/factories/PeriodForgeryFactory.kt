package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import java.time.Period

/**
 * A [ForgeryFactory] that will generate a [Period] instance.
 */
class PeriodForgeryFactory :
        ForgeryFactory<Period> {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): Period {
        return Period.of(
                forge.aPositiveInt(), // years
                forge.anInt(0, 12), // months
                forge.anInt(0, 32) // days
        )
    }
}