package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal interface Node {

    var parentNode: ParentNode?

    fun handle(c: Char): Boolean = false

    fun build(forge: Forge, builder: StringBuilder)

    fun verify()
}