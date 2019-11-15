package fr.xgouchet.elmyr.jvm.ext

import fr.xgouchet.elmyr.Forge
import java.util.concurrent.TimeUnit

/**
 * @param offset a pair of a value and time unit. The timestamp will be picked in a offset of ± the
 *               offset around the current timestamp
 * @return a long to be used as a timestamp, picked in the given offset around now
 */
fun Forge.aTimestamp(offset: Pair<Long, TimeUnit> = 365L to TimeUnit.DAYS): Long {
    val rangeMs = offset.second.toMillis(offset.first)
    val min = -rangeMs
    val now = System.currentTimeMillis()
    return now + aLong(min, rangeMs)
}
