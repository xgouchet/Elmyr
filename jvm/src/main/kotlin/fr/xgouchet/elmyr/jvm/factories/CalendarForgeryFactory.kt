package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.jvm.ext.aTimestamp
import java.util.Calendar
import java.util.Date

class CalendarForgeryFactory
    : ForgeryFactory<Calendar> {
    override fun getForgery(forge: Forge): Calendar {
        return Calendar.getInstance()
                .apply {
                    time = Date(forge.aTimestamp())
                }
    }
}