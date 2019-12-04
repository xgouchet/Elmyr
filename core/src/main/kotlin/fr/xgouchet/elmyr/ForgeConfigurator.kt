package fr.xgouchet.elmyr

/**
 * A class that can configure a Forge with additional factories.
 */
interface ForgeConfigurator {

    /**
     * Allows you to configure a [Forge] instance.
     */
    fun configure(forge: Forge)

    /**
     * A no-op implementation of [ForgeConfigurator].
     */
    object NoOp : ForgeConfigurator {
        override fun configure(forge: Forge) {}
    }
}
