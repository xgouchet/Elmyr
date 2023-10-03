package fr.xgouchet.buildsrc.settings

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.kotlinConfig() {
    tasks.withType(KotlinCompile::class.java).forEach {
        it.compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    extensions.findByType(KotlinProjectExtension::class.java)?.apply {
        jvmToolchain(17)
    }
}
