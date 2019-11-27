package fr.xgouchet.elmyr.junit4;

import fr.xgouchet.elmyr.annotation.Forgery;
import fr.xgouchet.elmyr.junit4.dummy.Bar;
import fr.xgouchet.elmyr.junit4.dummy.BarFactory;
import fr.xgouchet.elmyr.junit4.dummy.Foo;
import fr.xgouchet.elmyr.junit4.dummy.FooFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaReproducibilityTest {

    private static final long SEED = 0x5686B7805E0L;

    @Rule
    public ForgeRule forge = new ForgeRule(SEED)
            .withFactory(Foo.class, new FooFactory())
            .withFactory(Bar.class, new BarFactory());

    @Forgery
    private Foo fakeFoo = null;

    @Forgery
    public Bar fakeBar = null;

    @Before
    public void setUp() {
        checkSeedNotChanged();
        checkForgeryInjected();
    }

    @Test
    public void testRun1() {
    }

    @Test
    public void testRun2() {
    }

    private void checkSeedNotChanged() {
        assertThat(forge.getSeed()).isEqualTo(SEED);
    }

    private void checkForgeryInjected() {
        assertThat(fakeFoo.getI()).isEqualTo(-285046995);
        assertThat(fakeBar.getS()).isEqualTo("lwk1hkqd8m1pp235m6p5");
    }

}
