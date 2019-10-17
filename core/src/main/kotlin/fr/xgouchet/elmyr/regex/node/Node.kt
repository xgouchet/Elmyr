package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal interface Node {

    var parentNode: ParentNode?

    fun build(forge: Forge, builder: StringBuilder)

    fun check()

    fun getRoot(): ParentNode? {
        return parentNode?.getRoot() ?: parentNode ?: (this as? ParentNode)
    }
}