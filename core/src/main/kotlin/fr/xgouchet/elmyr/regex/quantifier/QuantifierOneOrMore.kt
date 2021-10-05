package fr.xgouchet.elmyr.regex.quantifier

import fr.xgouchet.elmyr.Forge

internal object QuantifierOneOrMore : Quantifier {

    override fun getQuantity(forge: Forge): Int = forge.anInt(1, Quantifier.MAX_UNBOUND_QUANTITY)

    override fun toString(): String {
        return "+"
    }
}
