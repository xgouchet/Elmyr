package fr.xgouchet.elmyr.spek

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeConfigurator
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Root

/**
 * Creates a Forge instance for the root [Spek] scope, already configured with a [ForgeLifecycleListener].
 * @param seeds a map containing the seeds to reset your test scopes (the key should be the qualified name of
 * your tests, with each parent group named separated by a "/"; e.g.: "group name/sub group name/test name").
 * @param configurator the [ForgeConfigurator] instance to use (leave empty if not necessary)
 */
fun Root.spekForge(
    seeds: Map<String, Long> = emptyMap(),
    configurator: ForgeConfigurator? = null
): Forge {

    val forge = Forge()
    configurator?.configure(forge)

    registerListener(ForgeLifecycleListener(forge, seeds))

    return forge
}
