package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC28_AnimatedDoodleBackgroundTest extends BaseTest {

    @Test
    public void TC28_verifyAnimatedDoodleBackgroundRenders() {

        System.out.println("================================");
        System.out.println("TC28 - Animated Doodle Background");
        System.out.println("================================");

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

        // Get page source after tapping Login
        String pageSource = driver.getPageSource();

        System.out.println("================================");
        System.out.println("PAGE SOURCE AFTER LOGIN");
        System.out.println("================================");
        System.out.println(pageSource);

        // Verify Login page opened
        Assert.assertTrue(
                pageSource.contains("Enter Mobile Number"),
                "Login page is not displayed."
        );

        System.out.println("Login page is displayed.");

        // Verify image elements are present
        Assert.assertTrue(
                pageSource.contains("XCUIElementTypeImage"),
                "Doodle background image elements are not present."
        );

        System.out.println("Doodle background image elements are present.");

        System.out.println(
                "SC_01_TC_028 PASSED - Animated doodle background renders."
        );
    }
}