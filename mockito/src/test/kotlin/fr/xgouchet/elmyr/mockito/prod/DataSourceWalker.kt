package fr.xgouchet.elmyr.mockito.prod

class DataSourceWalker(
    val source: DataSource
) : DataWalker {

    private var internalPosition = 0
    private var canLoop = false

    override fun getCurrent(): String {
        return source.getDataAt(internalPosition)
    }

    override fun walkForward() {
        internalPosition++
        val size = source.size()
        if (canLoop && internalPosition >= size){
            internalPosition -= size
        }
    }

    override fun walkBackward() {
        TODO("Not yet implemented")
    }

    override fun walkTo(position: Int) {
        internalPosition = position
    }

    override fun hasNext(): Boolean {
        TODO("Not yet implemented")
    }

    override fun peekNext(): String {
        TODO("Not yet implemented")
    }

    override fun setLoopEnabled(enabled: Boolean) {
        canLoop = enabled
    }
}