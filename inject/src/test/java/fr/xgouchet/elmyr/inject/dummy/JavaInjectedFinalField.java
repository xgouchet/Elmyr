package fr.xgouchet.elmyr.inject.dummy;

import fr.xgouchet.elmyr.annotation.Forgery;

public class JavaInjectedFinalField {

    @Forgery
    public final Foo publicFoo = new Foo(42);

}
