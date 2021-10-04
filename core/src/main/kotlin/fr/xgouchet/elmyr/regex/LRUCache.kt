package fr.xgouchet.elmyr.regex

internal class LRUCache<K : Any, V : Any>(
    val capacity: Int,
    val factory: (K) -> V
) {

    private val map: LinkedHashMap<K, V>

    private var hitCount = 0
    private var missCount = 0
    private var createCount = 0
    private var evictionCount = 0

    init {
        require(capacity > 0) { "Cache capacity must be greater than 0" }
        map = LinkedHashMap(capacity)
    }

    fun get(key: K): V {

        val existingValue: V?

        synchronized(map) {
            existingValue = map[key]
            if (existingValue != null) {
                hitCount++
                return existingValue
            }
            missCount++
        }

        // Call factory outside the synchronized block as it may block the thread for too long
        val createdValue = factory(key)
        createCount++

        val returnValue: V
        synchronized(map) {
            val conflictingValue = map.put(key, createdValue)
            returnValue = if (conflictingValue != null) {
                // There was a conflict so undo that last put
                map[key] = conflictingValue
                conflictingValue
            } else {
                createdValue
            }
        }

        trimToSize(capacity)

        return returnValue
    }

    private fun trimToSize(size: Int) {
        synchronized(map) {
            if (map.isEmpty()) return

            while (map.size > size) {
                val key = map.entries.iterator().next().key
                map.remove(key)
                evictionCount++
            }
        }
    }
}
