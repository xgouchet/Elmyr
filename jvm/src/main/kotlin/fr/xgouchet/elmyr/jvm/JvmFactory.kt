package fr.xgouchet.elmyr.jvm

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.jvm.factories.CalendarForgeryFactory
import fr.xgouchet.elmyr.jvm.factories.CurrencyForgeryFactory
import fr.xgouchet.elmyr.jvm.factories.DateForgeryFactory
import fr.xgouchet.elmyr.jvm.factories.DurationForgeryFactory
import fr.xgouchet.elmyr.jvm.factories.LocalDateForgeryFactory
import fr.xgouchet.elmyr.jvm.factories.LocalDateTimeForgeryFactory
import fr.xgouchet.elmyr.jvm.factories.LocalTimeForgeryFactory
import fr.xgouchet.elmyr.jvm.factories.LocaleForgeryFactory
import fr.xgouchet.elmyr.jvm.factories.PeriodForgeryFactory
import fr.xgouchet.elmyr.jvm.factories.RandomForgeryFactory
import fr.xgouchet.elmyr.jvm.factories.TimeZoneForgeryFactory
import fr.xgouchet.elmyr.jvm.factories.UUIDForgeryFactory

/**
 * Adds all the JVM factories to the current [Forge].
 * @return the same [Forge] instance, perfect to chain calls
 * @see [CalendarForgeryFactory]
 * @see [CurrencyForgeryFactory]
 * @see [DateForgeryFactory]
 * @see [LocaleForgeryFactory]
 * @see [RandomForgeryFactory]
 * @see [TimeZoneForgeryFactory]
 * @see [UUIDForgeryFactory]
 * @see [DurationForgeryFactory]
 * @see [PeriodForgeryFactory]
 * @see [LocalDateForgeryFactory]
 * @see [LocalDateTimeForgeryFactory]
 * @see [LocalTimeForgeryFactory]
 */
fun <T : Forge> T.useJvmFactories(): T {

    this.addFactory(CalendarForgeryFactory())
    this.addFactory(CurrencyForgeryFactory())
    this.addFactory(DateForgeryFactory())
    this.addFactory(LocaleForgeryFactory())
    this.addFactory(RandomForgeryFactory())
    this.addFactory(TimeZoneForgeryFactory())
    this.addFactory(UUIDForgeryFactory())
    this.addFactory(DurationForgeryFactory())
    this.addFactory(PeriodForgeryFactory())
    this.addFactory(LocalDateForgeryFactory())
    this.addFactory(LocalDateTimeForgeryFactory())
    this.addFactory(LocalTimeForgeryFactory())

    return this
}