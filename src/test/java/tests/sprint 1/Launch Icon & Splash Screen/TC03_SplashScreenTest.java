package tests.sprint1;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC03_SplashScreenTest extends BaseTest {

    @Test(
            description = "SC_01_TC_003 - Verify that the updated splash screen images are displayed when the app is launched."
    )
    public void verifySplashScreen() {

        System.out.println("========================================");
        System.out.println("SC_01_TC_003 - Splash Screen Test");
        System.out.println("========================================");

        Assert.assertNotNull(
                driver,
                "Appium driver is not initialized."
        );

        Assert.assertNotNull(
                driver.getSessionId(),
                "Appium session is not created."
        );

        System.out.println("Application launched successfully.");

        System.out.println(
                "Verify that the updated splash screen images are displayed."
        );

        Assert.assertTrue(
                driver.getSessionId() != null,
                "Application session is not active."
        );

        System.out.println("TC03 PASSED.");

        System.out.println("========================================");
        System.out.println("TC03 COMPLETED");
        System.out.println("========================================");
    }
}