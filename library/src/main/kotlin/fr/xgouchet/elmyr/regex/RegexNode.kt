package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * A Basic Regex Node
 *
 * @author Xavier F. Gouchet
 */
abstract class RegexNode(var parent: RegexParentNode? = null) {


    internal var quantifier: Quantifier = Quantifier.Companion.ONE

    fun build(forger: Forger, builder: StringBuilder) {
        val repeats = quantifier.getQuantity(forger)

        for (i in 0 until repeats) {
            buildIteration(forger, builder)
        }
    }

    abstract fun buildIteration(forger: Forger, builder: StringBuilder)

    override fun toString(): String {
        val b = StringBuilder()
        describe(b)
        quantifier.describe(b)
        return b.toString()
    }


    abstract fun describe(builder: StringBuilder)
}