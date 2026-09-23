package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC29_AnimatedDoodleDoesNotBlockTapsTest extends BaseTest {

    @Test
    public void TC29_verifyDoodleDoesNotBlockTaps() {

        System.out.println("================================");
        System.out.println("TC29 - Doodle Does Not Block Taps");
        System.out.println("================================");

        // Verify Welcome screen
        String pageSource = driver.getPageSource();

        Assert.assertTrue(
                pageSource.contains("Welcome to Bima Sugam"),
                "Welcome screen is not displayed."
        );

        System.out.println("Welcome screen is displayed.");

        // Tap Login button
        driver.findElement(
                AppiumBy.xpath("//*[@name='Login' or @label='Login']")
        ).click();

        System.out.println("Login button tapped.");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Verify Login screen opened
        String pageSourceAfterTap = driver.getPageSource();

        Assert.assertTrue(
                pageSourceAfterTap.contains("Enter Mobile Number"),
                "Login screen did not open after tapping Login."
        );

        System.out.println("Login screen opened successfully.");

        System.out.println(
                "SC_01_TC_029 PASSED - Animated doodle background does not block taps."
        );
    }
}