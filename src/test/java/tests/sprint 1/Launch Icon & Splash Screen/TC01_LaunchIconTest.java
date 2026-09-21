package tests.launchicon;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC01_LaunchIconTest extends BaseTest {

    @Test(
            description = "SC_01_TC_001 - Verify the app launches successfully and the session is active."
    )
    public void verifyLaunchApp() {

        System.out.println("TC01 - Launch Icon Test");

        Assert.assertNotNull(driver, "Driver is not initialized.");
        Assert.assertNotNull(driver.getSessionId(), "Appium session is not created.");

        System.out.println("Application launched successfully.");
        System.out.println("TC01 PASSED.");
    }
}
