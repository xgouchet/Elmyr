package fr.xgouchet.elmyr.inject.fixture;

import fr.xgouchet.elmyr.annotation.Forgery;
import org.mockito.Mock;

public class JavaInjected {

    @Forgery
    private Foo privateFoo;

    @Forgery
    protected Foo protectedFoo;

    @Forgery
    /*package*/ Foo packageFoo;

    @Forgery
    public Foo publicFoo;

    public Foo ignoredFooNoAnnotation;

    @Mock
    public Foo ignoredFooBadAnnotation;


    public Foo getPrivateFoo() {
        return privateFoo;
    }

    public Foo getProtectedFoo() {
        return protectedFoo;
    }

    public Foo getPackageFoo() {
        return packageFoo;
    }

    public Foo getPublicFoo() {
        return publicFoo;
    }

    public Foo getIgnoredFooNoAnnotation() {
        return ignoredFooNoAnnotation;
    }

    public Foo getIgnoredFooBadAnnotation() {
        return ignoredFooBadAnnotation;
    }
}
