package fr.xgouchet.elmyr.semantics.markov

import fr.xgouchet.elmyr.Forge
import kotlin.math.max

internal class MarkovTable(
    internal val tokens: CharArray,
    internal val chainLength: Int
) {
    private val table: IntArray

    // region Init

    init {
        require(chainLength > 0) { "Markov chain needs a dimension greater than 0" }

        val tableSize = (tokens.size.toLong() + 1) pow chainLength
        check(tableSize < MAX_TABLE_SIZE) {
            "Markov table dimension or token count is too large: $tableSize"
        }

        table = IntArray(tableSize.toInt()) { 0 }
    }

    fun sample(weight: Int, word: String) {
        require(word.length == chainLength) {
            "Sample has invalid length, expected $chainLength but was ${word.length}"
        }
        val index = index(word)
        val oldValue = table[index]
        table[index] = max(oldValue + weight, 0)
    }

    // endregion

    // region Generation

    fun generate(forge: Forge): String {
        return generateSequence(forge).joinToString("")
    }

    private fun generateSequence(forge: Forge): Sequence<Char> {
        val dataWindow = Array<Char?>(chainLength) { null }

        return generateSequence {
            dataWindow.shiftLeftAndInsert('?')

            val total = getTotalOptions(dataWindow)
            var sum = getOptions(dataWindow)

            val choice = when (total) {
                0 -> 0
                1 -> 1
                else -> forge.anInt(1, total)
            }
            dataWindow[chainLength - 1] = null
            var genToken: Char? = null
            if (sum < choice) {
                tokens.forEach { token ->
                    val options = getOptions(dataWindow)
                    if ((choice > sum) && (choice <= (sum + options))) {
                        dataWindow[chainLength - 1] = token
                        genToken = token
                    }
                    sum += options
                }
            }
            genToken
        }
    }

    // endregion

    // region Internals

    private fun index(data: Array<Char?>): Int {
        return index(data.word(NULL_CHAR))
    }

    private fun index(word: String): Int {
        var index = 0
        word.forEach { c ->
            val i = if (c == NULL_CHAR) tokens.size else tokens.indexOf(c)
            check(i >= 0 && i <= tokens.size) {
                "Index for token '$c' is out of bounds"
            }
            index = (index * (tokens.size + 1)) + i
        }
        return index
    }

    private fun getTotalOptions(dataWindow: Array<Char?>): Int {
        // check null
        dataWindow[chainLength - 1] = null
        var total = table[index(dataWindow)]

        // check known options
        tokens.forEach {
            dataWindow[chainLength - 1] = it
            total += table[index(dataWindow)]
        }
        return total
    }

    private fun getOptions(dataWindow: Array<Char?>): Int {
        return table[index(dataWindow)]
    }

    // endregion

    companion object {
        private const val MAX_TABLE_SIZE = 0xFFFFFFF

        internal const val NULL_CHAR = '∅'
    }
}
