package fr.xgouchet.elmyr.junit4.fixture

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeryFactory

internal class FooFactory :
    ForgeryFactory<Foo> {

    override fun getForgery(forge: Forge): Foo {
        return Foo(forge.anInt())
    }
}
