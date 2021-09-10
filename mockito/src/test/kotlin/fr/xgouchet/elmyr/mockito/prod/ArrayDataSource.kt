package fr.xgouchet.elmyr.mockito.prod

class ArrayDataSource(val size: Int) : DataSource {

    private val data = Array(size) { "" }

    override fun getDataAt(position: Int): String {
        if (position < 0) {
            throw IndexOutOfBoundsException("Can't get data at index $position.")
        }
        if (position >= size) {
            throw IndexOutOfBoundsException("Can't get data at index $position (data source only has $size elements).")
        }
        return data[position]
    }

    override fun size(): Int {
        return size
    }

    override fun put(position: Int, value: String) {
        data[position] = value
    }
}