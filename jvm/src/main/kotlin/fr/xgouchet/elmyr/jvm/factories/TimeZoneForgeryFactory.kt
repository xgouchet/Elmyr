package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import java.util.TimeZone

/**
 * A [ForgeryFactory] that will generate a [TimeZone] based on the available timezones on the
 * current system.
 */
class TimeZoneForgeryFactory :
    ForgeryFactory<TimeZone> {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): TimeZone {
        val availableIds = TimeZone.getAvailableIDs()
        val timeZoneId = forge.anElementFrom(*availableIds)
        return TimeZone.getTimeZone(timeZoneId)
    }
}