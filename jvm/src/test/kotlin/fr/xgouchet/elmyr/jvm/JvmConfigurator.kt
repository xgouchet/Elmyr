package fr.xgouchet.elmyr.jvm

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeConfigurator

class JvmConfigurator : ForgeConfigurator {
    override fun configure(forge: Forge) {
        forge.useJvmFactories()
    }
}
