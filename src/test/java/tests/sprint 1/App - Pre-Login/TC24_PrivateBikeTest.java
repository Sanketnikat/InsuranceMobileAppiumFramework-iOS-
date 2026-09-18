package tests.sprint1;

import framework.BaseTest;
import locators.IOSLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

public class TC24_PrivateBikeTest extends BaseTest {

    @Test(
            description = "TC24 - Verify Private Bike product tile navigation",
            groups = {"sprint1", "pre-login"}
    )
    public void TC24_privateBikeTileNavigation() {

        LoginPage loginPage = new LoginPage(driver);

        step("Open Login page");
        loginPage.clickLogin();

        step("Scroll to Private Bike insurance tile");
        boolean visible = scrollDownUntilVisible(IOSLocators.PRIVATE_BIKE_INSURANCE);
        Assert.assertTrue(
                visible,
                "Private Bike product tile is not displayed."
        );

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement bikeTile = wait.until(
                ExpectedConditions.elementToBeClickable(IOSLocators.PRIVATE_BIKE_INSURANCE)
        );

        step("Tap Private Bike tile");
        bikeTile.click();

        step("Verify Private Bike product flow opens");
        Assert.assertTrue(
                driver.getPageSource().contains("Bike Insurance"),
                "User was not navigated to the correct Private Bike product flow."
        );
    }

    private boolean scrollDownUntilVisible(By locator) {
        for (int attempt = 0; attempt < 8; attempt++) {
            if (isVisible(locator)) {
                return true;
            }

            try {
                driver.executeScript("mobile: scroll", Map.of("direction", "down"));
                Thread.sleep(500);
            } catch (Exception ignored) {
            }
        }

        return isVisible(locator);
    }

    private boolean isVisible(By locator) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator))
                    .isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }
}