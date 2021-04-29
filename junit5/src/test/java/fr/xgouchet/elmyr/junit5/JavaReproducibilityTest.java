package fr.xgouchet.elmyr.junit5;

import fr.xgouchet.elmyr.Forge;
import fr.xgouchet.elmyr.annotation.FloatForgery;
import fr.xgouchet.elmyr.annotation.Forgery;
import fr.xgouchet.elmyr.annotation.IntForgery;
import fr.xgouchet.elmyr.junit5.fixture.Bar;
import fr.xgouchet.elmyr.junit5.fixture.Foo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(ForgeExtension.class)
@ForgeConfiguration(value = JavaAnnotationTest.Configurator.class, seed = JavaReproducibilityTest.SEED)
public class JavaReproducibilityTest {

    public static final long SEED = 0x85F04771A07L;

    @Forgery
    private Foo fakeFoo = null;

    @Forgery
    public Bar fakeBar = null;

    @BeforeEach
    public void setUp(Forge forge) {
        checkSeedNotChanged(forge);
        checkForgeryInjected();
    }

    @Test
    public void testRun1() {
    }

    @Test
    public void testRun2() {
    }

    @Test
    public void testRun3(@Forgery Foo foo, @Forgery Bar bar) {
        assertThat(foo.getI()).isEqualTo(-1314996441);
        assertThat(bar.getS()).isEqualTo("xiqcgqfmbjoaevbo");
    }

    @Test
    public void testRun4(@FloatForgery float f, @IntForgery int i) {
        assertThat(f).isEqualTo(3.1084288E38f);
        assertThat(i).isEqualTo(177874237);
    }

    private void checkSeedNotChanged(Forge forge) {
        assertThat(forge.getSeed()).isEqualTo(SEED);
    }

    private void checkForgeryInjected() {
        assertThat(fakeFoo.getI()).isEqualTo(-119626925);
        assertThat(fakeBar.getS()).isEqualTo("ttcji");
    }

}
