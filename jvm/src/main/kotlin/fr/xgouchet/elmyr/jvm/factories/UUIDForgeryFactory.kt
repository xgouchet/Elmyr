package fr.xgouchet.elmyr.jvm.factories

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory
import java.util.UUID

class UUIDForgeryFactory
    : ForgeryFactory<UUID>{
    override fun getForgery(forge: Forge): UUID {
        val mostSigBits = forge.aLong()
        val leastSigBits = forge.aLong()
        return UUID(
                mostSigBits,
                leastSigBits
        )
    }
}