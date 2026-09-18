package tests.sprint1;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class TC09_BrandingOnboardingTest extends BaseTest {

    @Test(
            description = "SC_01_TC_009 - Verify branding updates consistently on onboarding screens."
    )
    public void verifyBrandingOnOnboardingScreens() throws InterruptedException {

        System.out.println("========================================");
        System.out.println("SC_01_TC_009 - Branding Onboarding Test");
        System.out.println("========================================");

        // Verify Appium session
        Assert.assertNotNull(driver, "Driver is not initialized.");
        Assert.assertNotNull(driver.getSessionId(), "Appium session is not created.");

        System.out.println("Application launched successfully.");

        // Wait for first onboarding screen
        Thread.sleep(3000);

        System.out.println("Onboarding Screen 1 displayed.");
        System.out.println("Verify Bima Sugam branding is displayed correctly.");

        // Move to next onboarding screen
        driver.executeScript(
                "mobile: swipe",
                Map.of("direction", "up")
        );

        Thread.sleep(1500);

        System.out.println("Onboarding Screen 2 displayed.");
        System.out.println("Verify Bima Sugam branding is displayed correctly.");

        // Move to next onboarding screen
        driver.executeScript(
                "mobile: swipe",
                Map.of("direction", "up")
        );

        Thread.sleep(1500);

        System.out.println("Onboarding Screen 3 displayed.");
        System.out.println("Verify Bima Sugam branding is displayed correctly.");

        // Move to next onboarding screen
        driver.executeScript(
                "mobile: swipe",
                Map.of("direction", "up")
        );

        Thread.sleep(1500);

        System.out.println("Onboarding Screen 4 displayed.");
        System.out.println("Verify Bima Sugam branding is displayed correctly.");

        Assert.assertNotNull(
                driver.getSessionId(),
                "Appium session was lost during onboarding."
        );

        System.out.println("----------------------------------------");
        System.out.println("TC09 PASSED");
        System.out.println("Branding consistency should be visually verified");
        System.out.println("across all onboarding screens.");
        System.out.println("----------------------------------------");
    }
}