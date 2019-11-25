package fr.xgouchet.elmyr.inject.dummy;

import fr.xgouchet.elmyr.annotation.Forgery;

public class JavaInjectedChild extends JavaInjected{

    @Forgery
    public Foo childFoo;

    @Deprecated
    public String deprecated;

    public Foo getChildFoo() {
        return childFoo;
    }
}
