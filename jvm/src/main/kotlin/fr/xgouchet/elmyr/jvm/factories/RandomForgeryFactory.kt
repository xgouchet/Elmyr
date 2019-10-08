package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import java.util.Random

class RandomForgeryFactory
    : ForgeryFactory<Random>{
    override fun getForgery(forge: Forge): Random {
        val seed = forge.aLong()
        return Random(seed)
    }
}