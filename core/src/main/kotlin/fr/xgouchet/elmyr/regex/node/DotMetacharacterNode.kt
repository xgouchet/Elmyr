package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class DotMetacharacterNode(
    override var parentNode: ParentNode?
) : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        builder.append(forge.aChar())
    }

    override fun check() {}

    // endregion
}