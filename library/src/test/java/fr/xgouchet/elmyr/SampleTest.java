package fr.xgouchet.elmyr;

import fr.xgouchet.elmyr.junit.JunitForger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author Xavier F. Gouchet
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class SampleTest {

    @Rule public JunitForger forger = new JunitForger();

    @Before
    public void resetForger() {
        forger.reset(0x1556f664);
    }

    @Test
    public void computeMultiplication() {
        int a = forger.aSmallInt();
        int b = forger.aSmallInt();

        int c = a * b;

        assertThat(c).isEqualTo(31160);
        assertThat(c).isLessThan(0);

    }
}
