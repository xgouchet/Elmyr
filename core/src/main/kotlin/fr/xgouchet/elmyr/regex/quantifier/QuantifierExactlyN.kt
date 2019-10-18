package fr.xgouchet.elmyr.regex.quantifier

import fr.xgouchet.elmyr.Forge

internal class QuantifierExactlyN(
    private val n: Int
) : Quantifier {

    override fun getQuantity(forge: Forge): Int = n
}