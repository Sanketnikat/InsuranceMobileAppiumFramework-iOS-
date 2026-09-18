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

public class TC23_PrivateCarTest extends BaseTest {

    @Test(
            description = "TC23 - Verify Private Car product tile navigation",
            groups = {"sprint1", "pre-login"}
    )
    public void TC23_privateCarTileNavigation() {

        LoginPage loginPage = new LoginPage(driver);

        step("Open Login page");
        loginPage.clickLogin();

        step("Scroll to Private Car insurance tile");
        boolean visible = scrollDownUntilVisible(IOSLocators.PRIVATE_CAR_INSURANCE);

        Assert.assertTrue(
                visible,
                "Private Car product tile is not displayed."
        );

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(15)
        );

        WebElement carTile = wait.until(
                ExpectedConditions.elementToBeClickable(
                        IOSLocators.PRIVATE_CAR_INSURANCE
                )
        );

        step("Tap Private Car tile");
        carTile.click();

        step("Verify Private Car product flow opens");

        Assert.assertTrue(
                driver.getPageSource().contains("Car Insurance"),
                "User was not navigated to the correct Private Car product flow."
        );
    }

    private boolean scrollDownUntilVisible(By locator) {

        for (int attempt = 0; attempt < 8; attempt++) {

            if (isVisible(locator)) {
                return true;
            }

            try {
                driver.executeScript(
                        "mobile: scroll",
                        Map.of("direction", "down")
                );

                Thread.sleep(500);

            } catch (Exception ignored) {
            }
        }

        return isVisible(locator);
    }

    private boolean isVisible(By locator) {

        try {
            return new WebDriverWait(
                    driver,
                    Duration.ofSeconds(5)
            )
                    .until(
                            ExpectedConditions.visibilityOfElementLocated(locator)
                    )
                    .isDisplayed();

        } catch (Exception ignored) {
            return false;
        }
    }
}