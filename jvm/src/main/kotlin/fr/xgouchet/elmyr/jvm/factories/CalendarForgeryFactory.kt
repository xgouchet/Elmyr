package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.jvm.ext.aTimestamp
import java.util.Calendar
import java.util.Date

/**
 * A [ForgeryFactory] that will generate a [Calendar] instance with a date and time set within one
 * year of the current time.
 */
class CalendarForgeryFactory :
    ForgeryFactory<Calendar> {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): Calendar {
        return Calendar.getInstance()
                .apply {
                    time = Date(forge.aTimestamp())
                }
    }
}