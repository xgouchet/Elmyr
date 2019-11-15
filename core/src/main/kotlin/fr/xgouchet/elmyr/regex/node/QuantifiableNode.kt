package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.regex.quantifier.Quantifier

internal interface QuantifiableNode : Node {

    fun handleQuantifier(quantifier: Quantifier)
}
