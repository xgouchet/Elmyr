package fr.xgouchet.buildsrc.settings

import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

inline fun <reified T : Any> Project.extensionConfig(
    crossinline configure: T.() -> Unit
): T? {
    val ext: T? = extensions.findByType(T::class)
    ext?.configure()
    return ext
}
