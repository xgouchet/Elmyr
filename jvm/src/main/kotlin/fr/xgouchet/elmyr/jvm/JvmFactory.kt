package fr.xgouchet.elmyr.jvm

import fr.xgouchet.elmyr.Forge

fun <T : Forge> T.withJvmFactories() : T {


    return this
}