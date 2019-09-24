package fr.xgouchet.elmyr.junit5;


import fr.xgouchet.elmyr.Forge;
import fr.xgouchet.elmyr.annotation.Forgery;
import fr.xgouchet.elmyr.junit5.dummy.Foo;
import fr.xgouchet.elmyr.junit5.dummy.FooFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;

class JavaAnnotationTest {

    @RegisterExtension
    static final ForgeExtension FORGE = new ForgeExtension()
            .withFactory(Foo.class, new FooFactory());

    private static Long memoizedSeed = null;
    private static Foo memoizedFoo = null;
    private static int injectedFooCount = 0;

    @Forgery
    public Foo fakeFoo;

    @Test
    void injectForge(Forge forge) {
        assertThat(forge).isNotNull();
        checkSeedChanged(forge);
        checkForgeryInjectedInField();
    }

    @Test
    void injectForge2(Forge forge) {
        assertThat(forge).isNotNull();
        checkSeedChanged(forge);
        checkForgeryInjectedInField();
    }

    @Test
    void injectForgery(@Forgery Foo foo) {
        assertThat(foo).isNotNull();
        checkForgeryInjectedInField();
    }


    private void checkSeedChanged(Forge forge) {
        Long previousSeed = memoizedSeed;
        if (previousSeed != null) {
            assertThat(forge.getSeed()).isNotEqualTo(previousSeed.longValue());
        }
        memoizedSeed = forge.getSeed();
    }

    private void checkForgeryInjectedInField() {
        Foo previousFoo = memoizedFoo;
        assertThat(fakeFoo).isNotNull();
        if (previousFoo != null) {
            assertThat(fakeFoo).isNotEqualTo(previousFoo);
        }
        memoizedFoo = fakeFoo;
    }

}
