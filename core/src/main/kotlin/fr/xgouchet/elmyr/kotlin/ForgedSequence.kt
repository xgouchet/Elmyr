package fr.xgouchet.elmyr.kotlin

internal class ForgedSequence<T>(
    val size: Int,
    val generate: () -> T
) : Sequence<T> {

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        var count = 0
        var nextItem: T? = null
        var nextState: Int = -2 // -2 for initial unknown, -1 for next unknown, 0 for done, 1 for continue

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