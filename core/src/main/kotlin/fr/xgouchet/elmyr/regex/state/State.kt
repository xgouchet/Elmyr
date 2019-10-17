package fr.xgouchet.elmyr.regex.state

import fr.xgouchet.elmyr.regex.node.Node

internal interface State {

    fun handleChar(c: Char): State

    fun getRoot(): Node
}