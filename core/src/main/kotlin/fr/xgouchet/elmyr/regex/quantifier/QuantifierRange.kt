package fr.xgouchet.elmyr.regex.quantifier

import fr.xgouchet.elmyr.Forge

internal class QuantifierRange(
    private val n: Int,
    private val m: Int
) : Quantifier {

    override fun getQuantity(forge: Forge): Int = forge.anInt(n, m + 1)
}