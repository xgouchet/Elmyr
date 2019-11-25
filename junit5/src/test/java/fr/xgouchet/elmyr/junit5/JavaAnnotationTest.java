package fr.xgouchet.elmyr.junit5;


import fr.xgouchet.elmyr.Forge;
import fr.xgouchet.elmyr.ForgeConfigurator;
import fr.xgouchet.elmyr.annotation.*;
import fr.xgouchet.elmyr.junit5.dummy.Bar;
import fr.xgouchet.elmyr.junit5.dummy.BarFactory;
import fr.xgouchet.elmyr.junit5.dummy.Foo;
import fr.xgouchet.elmyr.junit5.dummy.FooFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ForgeExtension.class)
@ForgeConfiguration(JavaAnnotationTest.Configurator.class)
class JavaAnnotationTest {

    public static class Configurator implements ForgeConfigurator {
        @Override
        public void configure(@NotNull Forge forge) {
            forge.addFactory(Foo.class, new FooFactory());
            forge.addFactory(Bar.class, new BarFactory());
        }
    }

    private static Long memoizedSeed = null;
    private static Foo memoizedFoo = null;
    private static int injectedFooCount = 0;

    @Forgery
    public Foo fakeFoo;

    @Forgery
    public List<Foo> fakeFooList;

    @Forgery
    public Set<Foo> fakeFooSet;

    @Forgery
    public Map<Foo, Bar> fakeFooMap;

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

    @Test
    void injectForgeryCollection(@Forgery Collection<Foo> foo) {
        assertThat(foo).isNotNull().isNotEmpty()
                .allMatch(f -> f instanceof Foo);
        checkForgeryInjectedInField();
    }

    @Test
    void injectForgeryList(@Forgery List<Foo> foo) {
        assertThat(foo).isNotNull().isNotEmpty()
                .allMatch(f -> f instanceof Foo);
        checkForgeryInjectedInField();
    }

    @Test
    void injectForgerySet(@Forgery Set<Foo> foo) {
        assertThat(foo).isNotNull().isNotEmpty()
                .allMatch(f -> f instanceof Foo);
        checkForgeryInjectedInField();
    }

    // region primitive

    @Test
    void injectIntWithDefaultRange(@IntForgery int i) {
        assertThat(i).isStrictlyBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
        checkForgeryInjectedInField();
    }

    @Test
    void injectIntWithCustomRange(@IntForgery(min = 13, max = 42) int i) {
        assertThat(i).isBetween(13, 41);
        checkForgeryInjectedInField();
    }

    @Test
    void injectIntWithGaussianDistribution(@IntForgery(mean = 42, standardDeviation = 7) int i) {
        assertThat(i).isBetween(-58, 142);
        checkForgeryInjectedInField();
    }

    @Test
    void injectLongWithDefaultRange(@LongForgery long l) {
        assertThat(l).isStrictlyBetween(Long.MIN_VALUE, Long.MAX_VALUE);
        checkForgeryInjectedInField();
    }

    @Test
    void injectLongWithCustomRange(@LongForgery(min = 13L, max = 42L) long l) {
        assertThat(l).isBetween(13L, 41L);
        checkForgeryInjectedInField();
    }

    @Test
    void injectLongWithGaussianDistribution(@LongForgery(mean = 42L, standardDeviation = 7L) long l) {
        assertThat(l).isBetween(-58L, 142L);
        checkForgeryInjectedInField();
    }

    @Test
    void injectFloatWithDefaultRange(@FloatForgery float f) {
        assertThat(f).isStrictlyBetween(-Float.MAX_VALUE, Float.MAX_VALUE);
        checkForgeryInjectedInField();
    }

    @Test
    void injectFloatWithCustomRange(@FloatForgery(min = 13f, max = 42f) float f) {
        assertThat(f).isBetween(13f, 41f);
        checkForgeryInjectedInField();
    }

    @Test
    void injectFloatWithGaussianDistribution(@FloatForgery(mean = 42f, standardDeviation = 7f) float f) {
        assertThat(f).isBetween(-58f, 142f);
        checkForgeryInjectedInField();
    }

    @Test
    void injectDoubleWithDefaultRange(@DoubleForgery double d) {
        assertThat(d).isStrictlyBetween(-Double.MAX_VALUE, Double.MAX_VALUE);
        checkForgeryInjectedInField();
    }

    @Test
    void injectDoubleWithCustomRange(@DoubleForgery(min = 13.0, max = 42.0) double d) {
        assertThat(d).isBetween(13.0, 42.0);
        checkForgeryInjectedInField();
    }

    @Test
    void injectDoubleWithGaussianDistribution(@DoubleForgery(mean = 42.0, standardDeviation = 7.0) double d) {
        assertThat(d).isBetween(-58.0, 142.0);
        checkForgeryInjectedInField();
    }

    // endregion

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

        assertThat(fakeFooList).isNotNull().isNotEmpty();
        assertThat(fakeFooSet).isNotNull().isNotEmpty();
        assertThat(fakeFooMap).isNotNull().isNotEmpty();
    }

}
