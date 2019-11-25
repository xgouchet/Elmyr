package fr.xgouchet.elmyr.inject.dummy;

import fr.xgouchet.elmyr.annotation.Forgery;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaInjectedUnknownGenerics {

    @Forgery
    public Comparable<Foo> unknownFoo;

}
