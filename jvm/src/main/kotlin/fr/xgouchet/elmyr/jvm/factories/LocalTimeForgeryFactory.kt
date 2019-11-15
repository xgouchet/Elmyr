package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import java.time.LocalTime
import java.util.concurrent.TimeUnit

/**
 * A [ForgeryFactory] that will generate a [LocalTime] instance.
 */
class LocalTimeForgeryFactory :
        ForgeryFactory<LocalTime> {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): LocalTime {
        return LocalTime.ofNanoOfDay(forge.aLong(0, MAX_NANOS))
    }

    companion object {
        private val MAX_NANOS = TimeUnit.DAYS.toNanos(1)
    }
}
