package fr.xgouchet.elmyr.regex.quantifier

import fr.xgouchet.elmyr.Forge

internal class QuantifierRange(val n: Int, val m: Int) : Quantifier {

    override fun getQuantity(forge: Forge): Int = forge.anInt(n, m + 1)
}