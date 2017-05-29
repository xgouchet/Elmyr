package fr.xgouchet.elmyr.junit;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author Xavier F. Gouchet
 */
public class RepeaterTest {

    static int counter = 0;
    static int counter_x5 = 0;

    @Rule public Repeater repeater = new Repeater(); // Yeah, 42, I know

    @Test
    public void baseTest_x42() {
        counter++;
        assertThat("").isNullOrEmpty();
        System.out.println("counter : " + counter);
    }

    @Test
    public void baseTest_x5() {
        counter_x5++;
        assertThat("").isNullOrEmpty();
        System.out.println("counter : " + counter_x5);
    }


    @AfterClass
    public static void studentsGoHome() {
        assertThat(counter).isEqualTo(42);
        assertThat(counter_x5).isEqualTo(5);
    }

    /*


    @Test
    public void baseTest_someIgnored() {
        assumeTrue(forger.aBool());
        counter++;
        assertThat("").isNullOrEmpty();
        System.out.println("counter : " + counter);
    }

    @Test
    public void baseTest_allIgnored() {
        assumeTrue(false);
        counter++;
        assertThat("").isNullOrEmpty();
        System.out.println("counter : " + counter);
    }

    @Test
    public void baseTest_someFailed() {
        assertTrue(forger.aBool());
        counter++;
        assertThat("").isNullOrEmpty();
        System.out.println("counter : " + counter);
    }

    @Test
    public void baseTest_allFailed() {
        assertTrue(false);
        counter++;
        assertThat("").isNullOrEmpty();
        System.out.println("counter : " + counter);
    }

     */
}
