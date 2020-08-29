package fr.xgouchet.elmyr.inject;

import fr.xgouchet.elmyr.Forge;
import fr.xgouchet.elmyr.ForgeryException;
import fr.xgouchet.elmyr.inject.dummy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JavaForgeryInjectorTest {

    private ForgeryInjector injector;
    private Forge forge;

    @BeforeEach
    void setUp() {
        injector = JavaInjectorFactory.javaInjector();
        forge = new Forge();
        forge.addFactory(Foo.class, new FooFactory());
        forge.addFactory(Bar.class, new BarFactory());
    }

    @Test
    void injectsPublicField() {
        JavaInjected injected = new JavaInjected();

        injector.inject(forge, injected);

        assertThat(injected.getPublicFoo()).isNotNull();
    }

    @Test
    void injectsPackageField() {
        JavaInjected injected = new JavaInjected();

        injector.inject(forge, injected);

        assertThat(injected.getPackageFoo()).isNotNull();
    }

    @Test
    void injectsProtectedField() {
        JavaInjected injected = new JavaInjected();

        injector.inject(forge, injected);

        assertThat(injected.getProtectedFoo()).isNotNull();
    }

    @Test
    void injectsPrivateField() {
        JavaInjected injected = new JavaInjected();

        injector.inject(forge, injected);

        assertThat(injected.getPrivateFoo()).isNotNull();
    }

    @Test
    void injectsDifferentInstances() {
        JavaInjected injected = new JavaInjected();

        injector.inject(forge, injected);

        assertThat(injected.getPublicFoo())
                .isNotSameAs(injected.getPackageFoo())
                .isNotSameAs(injected.getProtectedFoo())
                .isNotSameAs(injected.getPrivateFoo());

        assertThat(injected.getPackageFoo())
                .isNotSameAs(injected.getProtectedFoo())
                .isNotSameAs(injected.getPrivateFoo());

        assertThat(injected.getProtectedFoo())
                .isNotSameAs(injected.getPrivateFoo());
    }

    @Test
    void injectsPublicFieldFromParentClass() {
        JavaInjectedChild injected = new JavaInjectedChild();

        injector.inject(forge, injected);

        assertThat(injected.getPublicFoo()).isNotNull();
        assertThat(injected.getPackageFoo()).isNotNull();
        assertThat(injected.getProtectedFoo()).isNotNull();
        assertThat(injected.getPrivateFoo()).isNotNull();
        assertThat(injected.getChildFoo()).isNotNull();
    }

    @Test
    void injectsGenerics() {
        JavaInjectedGenerics injected = new JavaInjectedGenerics();

        injector.inject(forge, injected);

        assertThat(injected.privateFooList).isNotNull().isNotEmpty();
        assertThat(injected.privateFooSet).isNotNull().isNotEmpty();
        assertThat(injected.privateFooMap).isNotNull().isNotEmpty();
        assertThat(injected.privateFooCollection).isNotNull().isNotEmpty();
    }

    @Test
    void injectsPrimitives(){
        JavaInjectedPrimitives injected = new JavaInjectedPrimitives();

        injector.inject(forge, injected);

        assertThat(injected.publicInt).isNotEqualTo(0);
        assertThat(injected.publicLong).isNotEqualTo(0L);
        assertThat(injected.publicFloat).isNotEqualTo(0f);
        assertThat(injected.publicDouble).isNotEqualTo(0.0);

        assertThat(injected.publicIntList).isNotEmpty();
        assertThat(injected.publicLongSet).isNotEmpty();
        assertThat(injected.publicFloatCollection).isNotEmpty();
        assertThat(injected.publicDoubleSetList).isNotEmpty();
    }

    @Test
    void injectsStrings(){
        JavaInjectedStrings injected = new JavaInjectedStrings();

        injector.inject(forge, injected);

        assertThat(injected.publicHexaString).matches("[0-9a-zA-z]+");
        assertThat(injected.publicNumericalStringSet).isNotEmpty();
        assertThat(injected.publicRegex).matches("[abc]+");
        assertThat(injected.publicUUIDList).isNotEmpty();
    }

    @Test()
    void failsWhenMissingFactory() {
        JavaInjectedMissingFactory injected = new JavaInjectedMissingFactory();

        assertThrows(ForgeryException.class, () -> {
            injector.inject(forge, injected);
        });
    }

    @Test()
    void failsWhenInjectingFinalField() {
        JavaInjectedFinalField injected = new JavaInjectedFinalField();

        assertThrows(ForgeryInjectorException.class, () -> {
            injector.inject(forge, injected);
        });
    }

    @Test()
    void failsWhenInjectingUnknownGeneric() {
        JavaInjectedUnknownGenerics injected = new JavaInjectedUnknownGenerics();

        assertThrows(ForgeryInjectorException.class, () -> {
            injector.inject(forge, injected);
        });
    }
}
