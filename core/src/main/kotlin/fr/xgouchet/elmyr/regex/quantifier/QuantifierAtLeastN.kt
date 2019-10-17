package fr.xgouchet.elmyr.regex.quantifier

import fr.xgouchet.elmyr.Forge

internal class QuantifierAtLeastN(private val n: Int) : Quantifier {
    override fun getQuantity(forge: Forge): Int = forge.anInt(n, n + MAX_UNBOUND_QUANTITY)

    companion object {

        internal const val MAX_UNBOUND_QUANTITY = 0x20
    }
}