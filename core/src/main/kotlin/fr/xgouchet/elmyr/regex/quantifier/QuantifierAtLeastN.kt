package fr.xgouchet.elmyr.regex.quantifier

import fr.xgouchet.elmyr.Forge

internal class QuantifierAtLeastN(
    private val n: Int
) : Quantifier {

    override fun getQuantity(forge: Forge): Int {
        return forge.anInt(n, n + Quantifier.MAX_UNBOUND_QUANTITY)
    }


    override fun toString(): String {
        return "{$n,}"
    }

    companion object {

    }
}
