package fr.xgouchet.buildsrc.settings

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.kotlinConfig() {

    tasks.withType(KotlinCompile::class.java) {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }
}
