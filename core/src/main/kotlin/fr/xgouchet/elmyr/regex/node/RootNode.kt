package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

internal class RootNode : BaseParentNode() {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        for (child in children) {
            child.build(forge, builder)
        }
    }

    // endregion
}
