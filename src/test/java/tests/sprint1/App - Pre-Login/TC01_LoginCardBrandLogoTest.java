package tests.sprint1;

import framework.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC01_LoginCardBrandLogoTest extends BaseTest {

    @Test(description = "SC_01_TC_001 - Verify that the login card displays the brand logo")
    public void verifyLoginCardDisplaysBrandLogo() {

        System.out.println("========================================");
        System.out.println("SC_01_TC_001 - Login Card Brand Logo");
        System.out.println("========================================");

        System.out.println("Launching Bima Sugam application");

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        /*
         * Wait for the pre-login screen by asserting the Login element is visible.
         */
        WebElement loginElement = wait.until(
                driver -> driver.findElement(
                        By.xpath(
                                "//*[@name='Login' or @label='Login']"
                        )
                )
        );

        Assert.assertTrue(
                loginElement.isDisplayed(),
                "Pre-login screen is not available — Login element not displayed"
        );

        System.out.println("Pre-login screen displayed");

        /*
         * Locate the Bima Sugam brand logo.
         *
         * This locator is based on the accessible text/name.
         * If the logo has no accessible text, replace this
         * locator with the actual Appium Inspector attribute.
         */
        WebElement brandLogo = wait.until(
                driver -> driver.findElement(
                        By.xpath(
                                "//*[contains(@label,'Bima Sugam') " +
                                "or contains(@name,'Bima Sugam') " +
                                "or contains(@value,'Bima Sugam')]"
                        )
                )
        );

        Assert.assertTrue(
                brandLogo.isDisplayed(),
                "Brand logo is not displayed on the login card"
        );

        System.out.println("Login card is displayed");
        System.out.println("Brand logo is displayed on the login card");

        System.out.println("SC_01_TC_001 PASSED");
    }
}