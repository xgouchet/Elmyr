package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.jvm.ext.aTimestamp
import java.util.Date

/**
 * A [ForgeryFactory] that will generate a [Date] instance with a date and time set within one year
 * of the current time.
 */
class DateForgeryFactory :
    ForgeryFactory<Date> {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): Date {
        return Date(forge.aTimestamp())
    }
}
