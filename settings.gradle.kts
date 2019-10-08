import fr.xgouchet.buildsrc.Dependencies

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == Dependencies.PluginNamespaces.Kotlin) {
                useVersion(Dependencies.Versions.Kotlin)
            } else if (requested.id.namespace == Dependencies.PluginNamespaces.Detetk) {
                useVersion(Dependencies.Versions.Detekt)
            } else if (requested.id.namespace == Dependencies.PluginNamespaces.DependencyVersion) {
                useVersion(Dependencies.Versions.DependencyVersion)
            } else if (requested.id.namespace == Dependencies.PluginNamespaces.KtLint) {
                useVersion(Dependencies.Versions.KtLint)
            } else {
                println("⋄⋄⋄ namespace:${requested.id.namespace} / name:${requested.id.name}")
            }
        }
    }
}

include(":core")
include(":inject")
include(":junit4")
include(":junit5")
include(":jvm")
