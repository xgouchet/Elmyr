package fr.xgouchet.elmyr.mockito.prod

interface DataWalker {

    fun getCurrent(): String

    fun walkForward()

    fun walkBackward()

    fun walkTo(position: Int)

    fun hasNext(): Boolean

    fun peekNext(): String

    fun setLoopEnabled(enabled: Boolean)
}