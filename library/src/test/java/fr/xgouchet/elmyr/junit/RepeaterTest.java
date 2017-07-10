package fr.xgouchet.elmyr.junit;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.MultipleFailureException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assume.assumeThat;

/**
 * @author Xavier F. Gouchet
 */
public class RepeaterTest {

    static int counter_x42 = 0;
    static int counter_x5 = 0;

    @Rule public Repeater repeater = new Repeater();
    @Rule public JUnitForger forger = new JUnitForger();


    @Test
    @Repeat(count = 42)
    public void baseTest_x42() {
        counter_x42++;
        assertThat("").isNullOrEmpty();
    }

    @Test
    @Repeat(count = 5)
    public void baseTest_x5() {
        counter_x5++;
        assertThat("").isNullOrEmpty();
        System.out.println(forger.getSeed());
    }

    @Test
    @Repeat(count = 100, failureThreshold = 50)
    public void test_failing_sometimes() {
        assertThat(forger.aBool(0.75f)).isTrue();
    }

//    @Test
//    @Repeat(count = 100, failureThreshold = 50)
//    public void test_failing_often() {
//        assertThat(forger.aBool(0.25f)).isTrue();
//    }

    @Test
    @Repeat(count = 100, ignoreThreshold = 50)
    public void test_ignored_sometimes() {
        assumeThat(forger.aBool(0.75f), is(true));
    }

    @Test
    @Repeat(count = 100, ignoreThreshold = 50)
    public void test_ignored_often() {
        assumeThat(forger.aBool(0.25f), is(true));
    }

    @Test
    public void test_no_repeat(){
        // just testing
    }

    @AfterClass
    public static void studentsGoHome() {
        assertThat(counter_x42).isEqualTo(42);
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
