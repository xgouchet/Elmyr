package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import java.util.UUID

/**
 * A [ForgeryFactory] that will generate a [UUID] instance.
 * Using this factory rather than [UUID.randomUUID] makes sure that your tests are reproducible.
 */
class UUIDForgeryFactory :
    ForgeryFactory<UUID> {

    /** @inheritdoc */
    override fun getForgery(forge: Forge): UUID {
        val mostSigBits = forge.aLong()
        val leastSigBits = forge.aLong()
        return UUID(
                mostSigBits,
                leastSigBits
        )
    }
}
