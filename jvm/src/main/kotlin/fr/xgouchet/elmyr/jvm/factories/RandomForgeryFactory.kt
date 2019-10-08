package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import java.util.Random

/**
 * A [ForgeryFactory] that will generate a [Random] instance, with a random seed.
 *
 * Using this factory rather than `Random()` makes sure that your tests are reproducible.
 */
class RandomForgeryFactory :
    ForgeryFactory<Random> {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): Random {
        val seed = forge.aLong()
        return Random(seed)
    }
}