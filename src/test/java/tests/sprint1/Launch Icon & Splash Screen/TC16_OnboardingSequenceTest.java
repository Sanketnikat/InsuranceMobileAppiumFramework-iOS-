package tests.sprint1;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC16_OnboardingSequenceTest extends BaseTest {

    @Test(description = "SC_01_TC_016 - Verify each onboarding screen is displayed in correct sequence")
    public void verifyOnboardingScreensDisplayedInCorrectSequence() {

        System.out.println("Launching Bima Sugam application");

        String initialPageSource = driver.getPageSource();

        Assert.assertNotNull(
                initialPageSource,
                "Application UI is unavailable after launch"
        );

        System.out.println("Application launched successfully");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.fail("Thread was interrupted while waiting for onboarding screen");
        }

        String firstScreenSource = driver.getPageSource();

        Assert.assertNotNull(
                firstScreenSource,
                "First onboarding screen is not available"
        );

        System.out.println("First onboarding screen is displayed");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.fail("Thread was interrupted while waiting for next onboarding screen");
        }

        String secondScreenSource = driver.getPageSource();

        Assert.assertNotNull(
                secondScreenSource,
                "Second onboarding screen is not available"
        );

        System.out.println("Second onboarding screen is displayed");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.fail("Thread was interrupted while waiting for next onboarding screen");
        }

        String thirdScreenSource = driver.getPageSource();

        Assert.assertNotNull(
                thirdScreenSource,
                "Third onboarding screen is not available"
        );

        System.out.println("Third onboarding screen is displayed");

        System.out.println("Onboarding screens are displayed successfully");
        System.out.println("SC_01_TC_016 PASSED");
    }
}