package fr.xgouchet.elmyr.junit;

import fr.xgouchet.elmyr.Case;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author Xavier F. Gouchet
 */
public class JUnitForgerTest {

    @Rule public JUnitForger forger = new JUnitForger();

    @Test
    public void forgeString() {
        int size = forger.aSmallInt();
        String str = forger.aWord(Case.LOWER, size);

        assertThat(str)
                .hasSize(size);
    }
}
