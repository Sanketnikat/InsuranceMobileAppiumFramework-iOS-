package tests.sprint1;

import framework.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC27_TermLifeProductTileTest extends BaseTest {

    @Test
    public void TC27_verifyTermLifeProductTile() {

        // Find Term Life product tile
        By termLifeTile = By.xpath(
                "//*[contains(@name,'Term Life') or contains(@label,'Term Life')]"
        );

        // Verify Term Life tile is displayed
        Assert.assertTrue(
                driver.findElement(termLifeTile).isDisplayed(),
                "Term Life product tile is not displayed."
        );

        // Tap Term Life tile
        driver.findElement(termLifeTile).click();

        System.out.println(
                "SC_01_TC_027 PASSED - Term Life product tile was tapped."
        );
    }
}