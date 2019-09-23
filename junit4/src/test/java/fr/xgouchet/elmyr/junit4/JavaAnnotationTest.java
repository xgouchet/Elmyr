package fr.xgouchet.elmyr.junit4;

import fr.xgouchet.elmyr.junit4.dummy.Foo;
import fr.xgouchet.elmyr.junit4.dummy.FooFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaAnnotationTest {

    private static Long memoizedSeed = null;
    private static Foo memoizedFoo = null;

    @Rule
    public JUnitForge forge = new JUnitForge().withFactory(Foo.class, new FooFactory());

    @Forgery
    private Foo fakeFoo = null;

    @Before
    public void setUp() {
        checkSeedChanged();
        checkForgeryInjected();
    }

    @Test
    public void testRun1() {
    }

    @Test
    public void testRun2() {
    }

    private void checkSeedChanged() {
        Long previousSeed = memoizedSeed;
        if (previousSeed != null) {
            assertThat(forge.getSeed()).isNotEqualTo(previousSeed.longValue());
        }
        memoizedSeed = forge.getSeed();
    }

    private void checkForgeryInjected() {
        Foo previousFoo = memoizedFoo;
        if (previousFoo != null) {
            assertThat(fakeFoo).isNotEqualTo(previousFoo);
        }
        memoizedFoo = fakeFoo;
    }

}
