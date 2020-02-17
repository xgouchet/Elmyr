package fr.xgouchet.elmyr.kotlin

internal class ForgedSequence<T>(
    val size: Int,
    val generate: () -> T
) : Sequence<T> {

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        var count = 0

        override fun next(): T {
            if (count >= size) {
                throw NoSuchElementException()
            }

            val result = generate()
            count++

            return result
        }

        override fun hasNext(): Boolean {
            return count < size
        }
    }
}
