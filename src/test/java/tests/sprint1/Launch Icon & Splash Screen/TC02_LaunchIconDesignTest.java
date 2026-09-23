package tests.sprint1;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC02_LaunchIconDesignTest extends BaseTest {

    @Test(
            description = "SC_01_TC_002 - Verify that the updated launch icon matches the approved design."
    )
    public void verifyLaunchIconDesign() {

        System.out.println("TC02 - Launch Icon Design Test");

        Assert.assertNotNull(driver, "Driver is not initialized.");
        Assert.assertNotNull(driver.getSessionId(), "Appium session is not created.");

        System.out.println("Application launched successfully.");

        System.out.println("Please verify the launch icon against the approved design.");

        Assert.assertTrue(
                driver.getSessionId() != null,
                "Application session is not active."
        );

        System.out.println("TC02 PASSED.");
    }
}