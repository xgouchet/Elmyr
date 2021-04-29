package fr.xgouchet.elmyr.junit4.fixture

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory

internal class BarFactory :
    ForgeryFactory<Bar> {

    override fun getForgery(forge: Forge): Bar {
        return Bar(forge.anAlphaNumericalString())
    }
}
