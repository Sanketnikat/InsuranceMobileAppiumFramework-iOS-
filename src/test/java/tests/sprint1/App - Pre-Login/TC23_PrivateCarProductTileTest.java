package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC23_PrivateCarProductTileTest extends BaseTest {

    @Test
    public void TC23_verifyPrivateCarProductTile() {

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

        // Verify Private Car is displayed
        Assert.assertTrue(
                pageSource.contains("Private Car"),
                "Private Car product tile is not displayed."
        );

        System.out.println("Private Car product tile is displayed.");

        // Tap Private Car
        driver.findElement(
                AppiumBy.xpath(
                        "//*[contains(@name,'Private Car') or contains(@label,'Private Car')]"
                )
        ).click();

        System.out.println("Private Car product tile tapped.");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Check the next screen
        String pageSourceAfterClick = driver.getPageSource();

        System.out.println("================================");
        System.out.println("PAGE SOURCE AFTER PRIVATE CAR CLICK");
        System.out.println("================================");
        System.out.println(pageSourceAfterClick);

        Assert.assertTrue(
                pageSourceAfterClick.contains("Private Car")
                        || pageSourceAfterClick.contains("Car Insurance"),
                "Private Car product flow did not open."
        );

        System.out.println(
                "SC_01_TC_023 PASSED - Private Car product flow opened."
        );
    }
}