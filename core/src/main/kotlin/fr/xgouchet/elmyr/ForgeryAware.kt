package fr.xgouchet.elmyr

/**
 * Any class that extends this interface can use an Forgery properties "seamlessly".
 */
interface ForgeryAware {

    /**
     * A Forgery Aware class must be within reach of a [Forge] object.
     */
    val forge: Forge
}