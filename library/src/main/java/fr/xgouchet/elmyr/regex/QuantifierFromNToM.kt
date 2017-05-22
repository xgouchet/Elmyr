package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger


/**
 * @author Xavier F. Gouchet
 */
class QuantifierFromNToM(val n: Int, val m: Int) : Quantifier {

    override fun getQuantity(forger: Forger): Int {
        return forger.anInt(n, m + 1)
    }

    override fun describe(builder: StringBuilder) {
        builder.append("{")
                .append(n)
                .append(",")
                .append(m)
                .append("}")
    }
}