package tests.junit4;

import org.junit.Assert;
import org.junit.Test;

public class JUnit4SmokeTest {

    @Test
    public void junit4_is_available() {
        Assert.assertTrue("JUnit 4 is available in this project.", true);
    }
}
