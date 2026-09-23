package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class TC34_PreLoginScreenScrollTest extends BaseTest {

    @Test
    public void TC34_verifyPreLoginScreenScroll() {

        System.out.println("================================");
        System.out.println("TC34 - Pre-Login Screen Scroll");
        System.out.println("================================");

        // Step 1 - Tap Login
        driver.findElement(
                AppiumBy.xpath("//*[@name='Login' or @label='Login']")
        ).click();

        System.out.println("Login button tapped.");

        // Wait for Login screen
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Step 2 - Verify Login screen
        String pageSourceBeforeScroll = driver.getPageSource();

        Assert.assertTrue(
                pageSourceBeforeScroll.contains("Enter Mobile Number"),
                "Login screen is not displayed."
        );

        System.out.println("Login screen is displayed.");

        // Step 3 - Scroll up
        Map<String, Object> swipeArgs = new HashMap<>();

        swipeArgs.put("direction", "up");
        swipeArgs.put("velocity", 1000);

        driver.executeScript(
                "mobile: swipe",
                swipeArgs
        );

        System.out.println("Scroll up performed.");

        // Wait after scrolling
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Step 4 - Get page source after scrolling
        String pageSourceAfterScroll = driver.getPageSource();

        System.out.println("================================");
        System.out.println("PAGE SOURCE AFTER SCROLL");
        System.out.println("================================");
        System.out.println(pageSourceAfterScroll);

        // Step 5 - Verify Login screen is still available
        Assert.assertTrue(
                pageSourceAfterScroll.contains("Enter Mobile Number"),
                "Login screen is not available after scrolling."
        );

        System.out.println("Login screen is still displayed after scrolling.");

        System.out.println(
                "SC_01_TC_034 PASSED - Pre-login screen scroll was performed successfully."
        );
    }
}