package fr.xgouchet.elmyr.regex.quantifier

import fr.xgouchet.elmyr.Forge

internal interface Quantifier {
    fun getQuantity(forge: Forge): Int

    companion object {

        val MAYBE_ONE = QuantifierMaybeOne()

        val ZERO_OR_MORE = QuantifierAtLeastN(0)

        val ONE_OR_MORE = QuantifierAtLeastN(1)
    }
}