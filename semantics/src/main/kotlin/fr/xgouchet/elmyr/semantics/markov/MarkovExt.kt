package fr.xgouchet.elmyr.semantics.markov

/**
 * Compute this integer raised to the given power.
 */
internal infix fun Long.pow(power: Int): Long {
    return when {
        power == 0 -> 1L
        power == 1 -> this
        power > 1 -> this * (this pow (power - 1))
        else -> throw UnsupportedOperationException("Can't compute power of negative integer")
    }
}

/**
 *
 */
internal fun <T> Array<T>.shiftLeftAndInsert(newData: T) {
    for (i in 0 until size - 1) {
        this[i] = this[i + 1]
    }
    this[size - 1] = newData
}


internal fun Array<Char?>.word(nullChar: Char = '·'): String {
    return joinToString("") { (it ?: nullChar).toString() }
}

