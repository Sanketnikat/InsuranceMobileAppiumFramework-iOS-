package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC22_ExploreOurProductsHeadingTest extends BaseTest {

    @Test
    public void TC22_verifyExploreOurProductsHeading() {

        // Tap Login from Welcome screen
        driver.findElement(
                AppiumBy.xpath("//*[@name='Login' or @label='Login']")
        ).click();

        System.out.println("Login button tapped.");

        // Wait for next screen
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Get page source
        String pageSource = driver.getPageSource();

        System.out.println("================================");
        System.out.println("PAGE SOURCE AFTER LOGIN");
        System.out.println("================================");
        System.out.println(pageSource);

        // Verify Explore Our Products
        Assert.assertTrue(
                pageSource.contains("Explore Our Products"),
                "Explore Our Products heading is not displayed."
        );

        System.out.println(
                "SC_01_TC_022 PASSED - Explore Our Products heading is displayed."
        );
    }
}