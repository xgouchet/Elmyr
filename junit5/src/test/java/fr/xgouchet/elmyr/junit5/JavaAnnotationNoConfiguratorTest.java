package fr.xgouchet.elmyr.junit5;


import fr.xgouchet.elmyr.Forge;
import fr.xgouchet.elmyr.ForgeConfigurator;
import fr.xgouchet.elmyr.annotation.*;
import fr.xgouchet.elmyr.junit5.fixture.Bar;
import fr.xgouchet.elmyr.junit5.fixture.BarFactory;
import fr.xgouchet.elmyr.junit5.fixture.Foo;
import fr.xgouchet.elmyr.junit5.fixture.FooFactory;
import org.assertj.core.data.Offset;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ForgeExtension.class)
@ForgeConfiguration()
class JavaAnnotationNoConfiguratorTest {

    private static Long memoizedSeed = null;
    private static String memoizedString = null;


    @StringForgery
    public String fakeString;
    private static int injectedStringCount = 0;

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
        assertThat(f).isBetween(13f, 42f);
        checkForgeryInjectedInField();
    }

    @Test
    void injectFloatWithGaussianDistribution(@FloatForgery(mean = 42f, standardDeviation = 7f) float f) {
        assertThat(f).isCloseTo(42f, Offset.offset(21f));
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
        assertThat(d).isCloseTo(42.0, Offset.offset(21.0));
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
        String previousString = memoizedString;
        assertThat(fakeString).isNotNull();
        if (previousString != null) {
            assertThat(fakeString).isNotEqualTo(previousString);
        }
        memoizedString = fakeString;
    }
}
