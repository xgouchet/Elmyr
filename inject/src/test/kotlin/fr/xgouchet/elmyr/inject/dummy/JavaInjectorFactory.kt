package fr.xgouchet.elmyr.inject.dummy

import fr.xgouchet.elmyr.inject.DefaultForgeryInjector
import fr.xgouchet.elmyr.inject.ForgeryInjector

object JavaInjectorFactory {

    @JvmStatic
    fun javaInjector(): ForgeryInjector {
        return DefaultForgeryInjector()
    }
}