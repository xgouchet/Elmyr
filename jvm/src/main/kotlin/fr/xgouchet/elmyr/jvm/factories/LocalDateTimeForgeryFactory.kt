package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.jvm.ext.aTimestamp
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

/**
 * A [ForgeryFactory] that will generate a [LocalDateTime] instance with a date and time set within
 * one year of the current time.
 */
class LocalDateTimeForgeryFactory :
        ForgeryFactory<LocalDateTime> {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): LocalDateTime {
        val zoneOffset = forge.anElementFrom(
                ZoneOffset.ofHoursMinutesSeconds(
                        forge.anInt(0, 18),
                        forge.anInt(0, 60),
                        forge.anInt(0, 60)
                ),
                ZoneOffset.ofHoursMinutesSeconds(
                        -forge.anInt(0, 18),
                        -forge.anInt(0, 60),
                        -forge.anInt(0, 60)
                ),
                ZoneOffset.MAX,
                ZoneOffset.MIN
        )
        return LocalDateTime.ofEpochSecond(
                TimeUnit.MILLISECONDS.toSeconds(forge.aTimestamp()),
                forge.anInt(0, MAX_NANOS),
                zoneOffset
        )
    }

    companion object {
        private const val MAX_NANOS = 1000_000_000
    }
}
