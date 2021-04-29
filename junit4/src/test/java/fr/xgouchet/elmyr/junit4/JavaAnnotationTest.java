package fr.xgouchet.elmyr.junit4;

import fr.xgouchet.elmyr.annotation.Forgery;
import fr.xgouchet.elmyr.junit4.fixture.Bar;
import fr.xgouchet.elmyr.junit4.fixture.BarFactory;
import fr.xgouchet.elmyr.junit4.fixture.Foo;
import fr.xgouchet.elmyr.junit4.fixture.FooFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaAnnotationTest {

    private static Long memoizedSeed = null;
    private static Foo memoizedFoo = null;

    @Rule
    public ForgeRule forge = new ForgeRule()
            .withFactory(Foo.class, new FooFactory())
            .withFactory(Bar.class, new BarFactory());

    @Forgery
    private Foo fakeFoo = null;

    @Forgery
    public List<Foo> fakeFooList;

    @Forgery
    public Set<Foo> fakeFooSet;

    @Forgery
    public Map<Foo, Bar> fakeFooMap;

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
        assertThat(fakeFoo).isNotNull();
        if (previousFoo != null) {
            assertThat(fakeFoo).isNotEqualTo(previousFoo);
        }
        memoizedFoo = fakeFoo;

        assertThat(fakeFooList).isNotNull().isNotEmpty();
        assertThat(fakeFooSet).isNotNull().isNotEmpty();
        assertThat(fakeFooMap).isNotNull().isNotEmpty();
    }

}
