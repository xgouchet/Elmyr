package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge

/**
 * Describe an alternation, that is a choice between two patterns.
 * e.g.: ```/foo|bar/```
 */
internal class AlternationNode : BaseParentNode() {

    // region Node

    override fun build(forge: Forge, builder: StringBuilder) {
        val child = forge.anElementFrom(children)
        child.build(forge, builder)
    }

    override fun toRegex(): String {
        return children.joinToString("|") { it.toRegex() }
    }

    // endregion
}
