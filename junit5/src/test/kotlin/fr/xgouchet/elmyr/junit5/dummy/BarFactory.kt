package fr.xgouchet.elmyr.junit5.dummy

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory

internal class BarFactory :
    ForgeryFactory<Bar> {

    override fun getForgery(forge: Forge): Bar {
        return Bar(forge.anAlphabeticalString())
    }
}
