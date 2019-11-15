package fr.xgouchet.elmyr.regex.state

internal interface State {

    fun handleChar(c: Char): State

    fun handleEndOfRegex()
}
