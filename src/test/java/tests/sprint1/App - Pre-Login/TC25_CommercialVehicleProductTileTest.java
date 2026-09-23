package tests.sprint1;

import framework.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC25_CommercialVehicleProductTileTest extends BaseTest {

    @Test
    public void TC25_verifyCommercialVehicleProductTile() {

        // Find Commercial Vehicle tile
        By commercialVehicle = By.xpath(
                "//*[contains(@name,'Commercial Vehicle') or contains(@label,'Commercial Vehicle')]"
        );

        // Check tile is displayed
        Assert.assertTrue(
                driver.findElement(commercialVehicle).isDisplayed(),
                "Commercial Vehicle product tile is not displayed."
        );

        // Tap Commercial Vehicle
        driver.findElement(commercialVehicle).click();

        System.out.println(
                "SC_01_TC_025 PASSED - Commercial Vehicle product tile was tapped."
        );
    }
}