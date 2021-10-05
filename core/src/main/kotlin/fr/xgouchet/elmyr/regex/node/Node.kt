package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal interface Node {

    fun build(forge: Forge, builder: StringBuilder)

    fun toRegex() : String
}
