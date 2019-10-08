package fr.xgouchet.elmyr.jvm.ext

import fr.xgouchet.elmyr.Forge
import java.util.concurrent.TimeUnit

fun Forge.aTimestamp(range: Long = TimeUnit.DAYS.toMillis(365), unit: TimeUnit = TimeUnit.MILLISECONDS): Long {
    val rangeMs = unit.toMillis(range)
    val min = -rangeMs
    val now = System.currentTimeMillis()
    return now + aLong(min, rangeMs)
}