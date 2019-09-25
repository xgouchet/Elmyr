package fr.xgouchet.elmyr.inject

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.Forgery

/**
 * Defines a class capable of injecting forgeries on an object with [Forgery] annotated fields or
 * properties.
 */
interface ForgeryInjector {

    /**
     * Injects forgeries on all fields or properties of the target, using the given [Forge].
     * @param forge the forge to use. Make sure it has all the necessary factories.
     * @param target the instance whose fields/properties will be injected with newly create forgeries.
     */
    fun inject(forge: Forge, target: Any)
}