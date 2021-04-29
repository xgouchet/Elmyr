package fr.xgouchet.elmyr.inject.fixture;

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
