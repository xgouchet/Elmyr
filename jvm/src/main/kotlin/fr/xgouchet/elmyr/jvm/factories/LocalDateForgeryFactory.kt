package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.jvm.ext.aTimestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

/**
 * A [ForgeryFactory] that will generate a [LocalDateTime] instance.
 */
class LocalDateForgeryFactory :
        ForgeryFactory<LocalDate> {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): LocalDate {
        return LocalDate.ofEpochDay(
                TimeUnit.MILLISECONDS.toDays(forge.aTimestamp())
        )
    }
}
