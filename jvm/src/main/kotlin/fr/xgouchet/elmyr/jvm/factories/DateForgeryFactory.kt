package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.jvm.ext.aTimestamp
import java.util.Date

class DateForgeryFactory
    : ForgeryFactory<Date> {
    override fun getForgery(forge: Forge): Date {
        return Date(forge.aTimestamp())
    }
}