package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class DotMetacharacterNode : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        builder.append(forge.aChar())
    }

    // endregion
}