package fr.xgouchet.elmyr.regex

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import fr.xgouchet.elmyr.regex.node.Node

internal class RegexStringFactory(
    private val rootNode: Node
) : ForgeryFactory<String> {

    override fun getForgery(forge: Forge): String {
        val stringBuilder = StringBuilder()
        rootNode.build(forge, stringBuilder)
        return stringBuilder.toString()
    }
}
