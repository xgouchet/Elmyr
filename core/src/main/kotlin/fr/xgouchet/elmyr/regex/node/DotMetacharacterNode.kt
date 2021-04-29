package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

/**
 * Describe a dot character used as a wildcard.
 * e.g.: ```/foo./```
 */
internal class DotMetacharacterNode : Node {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        builder.append(forge.aChar())
    }

    // endregion
}
