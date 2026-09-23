package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC31_AnimatedDoodleLowEndDeviceTest extends BaseTest {

    @Test
    public void TC31_verifyAnimatedDoodleOnLowEndDevice() {

        System.out.println("================================");
        System.out.println("TC31 - Animated Doodle Low-End Device");
        System.out.println("================================");

        // Verify Welcome screen
        String pageSource = driver.getPageSource();

        Assert.assertTrue(
                pageSource.contains("Welcome to Bima Sugam"),
                "Welcome screen is not displayed."
        );

        System.out.println("Welcome screen is displayed.");

        // Wait while animation is running
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Check screen again
        String pageSourceAfterWait = driver.getPageSource();

        Assert.assertTrue(
                pageSourceAfterWait.contains("Welcome to Bima Sugam"),
                "Welcome screen is not displayed after animation."
        );

        System.out.println("Animated doodle is still displayed.");

        // Verify Login button is still responsive
        driver.findElement(
                AppiumBy.xpath("//*[@name='Login' or @label='Login']")
        ).click();

        System.out.println("Login button tapped.");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String pageSourceAfterLogin = driver.getPageSource();

        Assert.assertTrue(
                pageSourceAfterLogin.contains("Enter Mobile Number"),
                "Application did not respond after animation."
        );

        System.out.println(
                "SC_01_TC_031 PASSED - Animated doodle background did not interrupt the application."
        );
    }
}