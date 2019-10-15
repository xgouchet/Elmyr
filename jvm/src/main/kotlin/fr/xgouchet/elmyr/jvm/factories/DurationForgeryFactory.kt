package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import java.time.Duration

/**
 * A [ForgeryFactory] that will generate a [Duration] instance.
 */
class DurationForgeryFactory :
        ForgeryFactory<Duration> {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): Duration {
        return Duration.ofMillis(forge.aPositiveLong())
    }
}