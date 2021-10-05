package fr.xgouchet.elmyr.regex.quantifier

import fr.xgouchet.elmyr.Forge

internal object QuantifierMaybeOne : Quantifier {

    override fun getQuantity(forge: Forge): Int = if (forge.aBool()) 0 else 1

    override fun toString(): String {
        return "?"
    }
}
