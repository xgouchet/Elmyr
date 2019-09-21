package fr.xgouchet.elmyr

/**
 * A ForgeryFactory is a class able to generate an object of the desired type based on a given Forge instance.
 * @param T the type of the object to forge
 */
interface ForgeryFactory<out T : Any> {

    /**
     * @param forge the forge instance to use to generate a forgery
     * @return a new instance of type T, randomly generated with the help of the forge instance
     */
    fun getForgery(forge: Forge): T
}
