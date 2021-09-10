package fr.xgouchet.elmyr.mockito.prod

interface DataSource {

    fun getDataAt(position: Int): String

    fun size(): Int

    fun put(position: Int, data: String)
}