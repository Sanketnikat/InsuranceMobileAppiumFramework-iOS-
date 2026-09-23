package tests.sprint1;

import framework.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC26_HealthProductTileTest extends BaseTest {

    @Test
    public void TC26_verifyHealthProductTile() {

        // Find Health product tile
        By healthTile = By.xpath(
                "//*[contains(@name,'Health') or contains(@label,'Health')]"
        );

        // Verify Health tile is displayed
        Assert.assertTrue(
                driver.findElement(healthTile).isDisplayed(),
                "Health product tile is not displayed."
        );

        // Tap Health tile
        driver.findElement(healthTile).click();

        System.out.println(
                "SC_01_TC_026 PASSED - Health product tile was tapped."
        );
    }
}