import fr.xgouchet.buildsrc.Dependencies

pluginManagement {
    resolutionStrategy {
        eachPlugin {

            val version = when (requested.id.id) {
                Dependencies.PluginId.Kotlin -> Dependencies.Versions.Kotlin
                Dependencies.PluginId.DependencyVersion -> Dependencies.Versions.DependencyVersion
                Dependencies.PluginId.Detetk -> Dependencies.Versions.Detekt
                Dependencies.PluginId.KtLint -> Dependencies.Versions.KtLint
                Dependencies.PluginId.Dokka -> Dependencies.Versions.Dokka
                else -> {
                    when (requested.id.namespace) {
                        Dependencies.PluginNamespaces.Gradle -> println("Core Gradle plugin id:${requested.id.id}")
                        null -> println("Custom Gradle plugin id:${requested.id.id}")
                        else -> println("Unknown Gradle plugin id:${requested.id.id} / namespace:${requested.id.namespace} / name:${requested.id.name}")
                    }
                    null
                }
            }

            if (version != null) {
                useVersion(version)
            }
        }
    }
}

include(":core")
include(":inject")
include(":junit4")
include(":junit5")
include(":jvm")
include(":semantics")
include(":spek")
