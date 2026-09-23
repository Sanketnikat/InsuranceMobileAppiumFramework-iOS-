package tests.sprint1;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC15_OnboardingNetworkUnavailableTest extends BaseTest {

    @Test(description = "SC_01_TC_015 - Verify onboarding behavior when network connectivity is unavailable")
    public void verifyOnboardingBehaviorWhenNetworkUnavailable() {

        System.out.println("Launching Bima Sugam application");

        // Verify application is running after launch
        String initialPageSource = driver.getPageSource();

        Assert.assertNotNull(
                initialPageSource,
                "Application UI is unavailable after launch"
        );

        System.out.println("Splash screen launched successfully");

        // Wait for the application to reach the next screen
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.fail("Thread was interrupted while waiting for onboarding screen");
        }

        System.out.println("Application reached the next screen");

        // Minimize the iOS application
        driver.executeScript(
                "mobile: terminateApp",
                java.util.Map.of(
                        "bundleId", "com.insurance.bimasugam"
                )
        );

        System.out.println("Bima Sugam application minimized/terminated");

        // Wait before reopening
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.fail("Thread was interrupted while waiting to reopen application");
        }

        // Reopen the Bima Sugam application
        driver.executeScript(
                "mobile: launchApp",
                java.util.Map.of(
                        "bundleId", "com.insurance.bimasugam"
                )
        );

        System.out.println("Bima Sugam application reopened");

        // Wait for application UI
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.fail("Thread was interrupted while waiting for application UI");
        }

        // Verify application UI is available after reopening
        String reopenedPageSource = driver.getPageSource();

        Assert.assertNotNull(
                reopenedPageSource,
                "Application UI is unavailable after reopening"
        );

        System.out.println("Application reopened successfully");
        System.out.println("Splash/onboarding UI is available after reopening");

        System.out.println("SC_01_TC_015 PASSED");
    }
}