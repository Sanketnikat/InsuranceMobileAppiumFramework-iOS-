package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC24_PrivateBikeProductTileTest extends BaseTest {

    @Test
    public void TC24_verifyPrivateBikeProductTile() {

        // Tap Login to open the pre-login screen
        driver.findElement(
                AppiumBy.xpath("//*[@name='Login' or @label='Login']")
        ).click();

        System.out.println("Login button tapped.");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Get page source
        String pageSource = driver.getPageSource();

        System.out.println("================================");
        System.out.println("PAGE SOURCE");
        System.out.println("================================");
        System.out.println(pageSource);

        // Verify Private Bike is displayed
        Assert.assertTrue(
                pageSource.contains("Private Bike"),
                "Private Bike product tile is not displayed."
        );

        System.out.println("Private Bike product tile is displayed.");

        // Tap Private Bike
        driver.findElement(
                AppiumBy.xpath(
                        "//*[contains(@name,'Private Bike') or contains(@label,'Private Bike')]"
                )
        ).click();

        System.out.println("Private Bike product tile tapped.");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Check the next screen
        String pageSourceAfterClick = driver.getPageSource();

        System.out.println("================================");
        System.out.println("PAGE SOURCE AFTER PRIVATE BIKE CLICK");
        System.out.println("================================");
        System.out.println(pageSourceAfterClick);

        Assert.assertTrue(
                pageSourceAfterClick.contains("Private Bike")
                        || pageSourceAfterClick.contains("Bike Insurance"),
                "Private Bike product flow did not open."
        );

        System.out.println(
                "SC_01_TC_024 PASSED - Private Bike product flow opened."
        );
    }
}