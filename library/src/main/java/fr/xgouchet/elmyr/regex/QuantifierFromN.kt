package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger


/**
 * @author Xavier F. Gouchet
 */
class QuantifierFromN(val n: Int) : Quantifier {

    override fun getQuantity(forger: Forger): Int {
        return forger.anInt(n, n + 16)
    }

    override fun describe(builder: StringBuilder) {
        builder.append("{")
                .append(n)
                .append(",}")
    }
}