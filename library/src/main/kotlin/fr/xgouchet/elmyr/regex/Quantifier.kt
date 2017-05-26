package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * @author Xavier F. Gouchet
 */
interface Quantifier {

    fun getQuantity(forger: Forger): Int

    fun describe(builder: StringBuilder)

    companion object {

        val ONE = object : Quantifier {
            override fun getQuantity(forger: Forger): Int = 1

            override fun describe(builder: StringBuilder) {}
        }

        val MAYBE_ONE = object : Quantifier {
            override fun getQuantity(forger: Forger): Int = forger.anInt(0, 2)

            override fun describe(builder: StringBuilder) {
                builder.append("?")
            }
        }

        val ZERO_OR_MORE = object : Quantifier {
            override fun getQuantity(forger: Forger): Int = forger.anInt(0, 16)

            override fun describe(builder: StringBuilder) {
                builder.append("*")
            }
        }

        val ONE_OR_MORE = object : Quantifier {
            override fun getQuantity(forger: Forger): Int = forger.anInt(1, 16)

            override fun describe(builder: StringBuilder) {
                builder.append("+")
            }
        }
    }
}
