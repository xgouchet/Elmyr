package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forger

/**
 * @author Xavier F. Gouchet
 */
class QuantifierN(val n: Int) : Quantifier {

    override fun getQuantity(forger: Forger): Int {
        return n
    }

    override fun describe(builder: StringBuilder) {
        builder.append("{")
                .append(n)
                .append("}")
    }
}