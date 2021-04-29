package fr.xgouchet.elmyr.inject.fixture;

import fr.xgouchet.elmyr.annotation.Forgery;

public class JavaInjectedUnknownGenerics {

    @Forgery
    public Comparable<Foo> unknownFoo;

}
