package fr.xgouchet.elmyr

import java.util.Random

open class Forge {

    private val rng = Random()

    private val factories: MutableMap<Class<*>, ForgeryFactory<*>> = mutableMapOf()

    // region Reproducibility

    var seed: Long = System.nanoTime()
        set(value) {
            field = value
            rng.setSeed(seed)
        }

    // endregion

    // region Factory

    inline fun <reified T : Any> addFactory(forgeryFactory: ForgeryFactory<T>) {
        addFactory(T::class.java, forgeryFactory)
    }

    fun <T : Any> addFactory(clazz: Class<T>, forgeryFactory: ForgeryFactory<T>) {
        factories[clazz] = forgeryFactory
    }

    inline fun <reified T : Any> getForgery(): T {
        return getForgery(T::class.java)
    }

    fun <T : Any> getForgery(clazz: Class<T>): T {

        @Suppress("UNCHECKED_CAST")
        val strictMatch = factories[clazz] as? ForgeryFactory<T>

        return strictMatch?.getForgery(this) ?: getSubclassForgery(clazz)
    }

    private fun <T : Any> getSubclassForgery(clazz: Class<T>): T {

        val matches = factories.filterKeys {
            clazz.isAssignableFrom(it)
        }.values



        if (matches.isEmpty()) {
            throw IllegalArgumentException("Cannot create forgery for type ${clazz.canonicalName}.\n" +
                    "Make sure you provide a factory for this type.")
        } else {
            val factory = if (aBool()) matches.first() else matches.last()
            @Suppress("UNCHECKED_CAST")
            return (factory as ForgeryFactory<T>).getForgery(this)
        }
    }

    // endregion

    // region Bool

    /**
     * @param probability the probability the boolean will be true (default 0.5f)
     * @return a boolean
     */
    @JvmOverloads
    fun aBool(probability: Float = HALF_PROBABILITY): Boolean {
        return rng.nextFloat() < probability
    }

    // endregion

    companion object {

        internal const val HALF_PROBABILITY = 0.5f
    }

}
