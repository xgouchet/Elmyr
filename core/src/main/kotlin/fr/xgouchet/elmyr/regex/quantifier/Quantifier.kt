package fr.xgouchet.elmyr.regex.quantifier

import fr.xgouchet.elmyr.Forge

internal interface Quantifier {
    fun getQuantity(forge: Forge): Int

    companion object {

        internal val MAYBE_ONE = QuantifierMaybeOne

        internal val ZERO_OR_MORE = QuantifierZeroOrMore

        internal val ONE_OR_MORE = QuantifierOneOrMore

        internal const val MAX_UNBOUND_QUANTITY = 0x20
    }
}
