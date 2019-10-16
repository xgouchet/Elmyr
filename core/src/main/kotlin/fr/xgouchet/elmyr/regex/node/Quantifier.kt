package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal interface Quantifier {
    fun getQuantity(forge: Forge): Int

    companion object {

        val ONE = object : Quantifier {
            override fun getQuantity(forge: Forge): Int = 1
        }

        val MAYBE_ONE = object : Quantifier {
            override fun getQuantity(forge: Forge): Int = if (forge.aBool()) 1 else 0
        }

        val ZERO_OR_MORE = object : Quantifier {
            override fun getQuantity(forge: Forge): Int = forge.anInt(0, MAX_UNBOUND_QUANTITY)
        }

        val ONE_OR_MORE = object : Quantifier {
            override fun getQuantity(forge: Forge): Int = forge.anInt(1, MAX_UNBOUND_QUANTITY)
        }

        internal const val MAX_UNBOUND_QUANTITY = 0x20
    }
}