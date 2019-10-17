package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.regex.quantifier.Quantifier

internal interface ParentNode :
    Node {

    fun add(node: Node)

    fun remove(node: Node)

    fun isEmpty(): Boolean

    fun handleQuantifier(quantifier: Quantifier)
}