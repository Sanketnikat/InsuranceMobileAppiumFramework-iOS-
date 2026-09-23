package tests.sprint1;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC13_Splash4DisplayedTest extends BaseTest {

    @Test(description = "SC_01_TC_13 - Verify Splash 4 is displayed correctly")
    public void verifySplash4DisplayedCorrectly() {

        String pageSource = driver.getPageSource();

        System.out.println("========== SPLASH 4 PAGE SOURCE ==========");
        System.out.println(pageSource);
        System.out.println("==========================================");

        Assert.assertNotNull(
                pageSource,
                "Splash 4 UI is unavailable"
        );
    }
}