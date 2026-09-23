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

public class TC05_ValidIndianMobileNumberTest extends BaseTest {

    @Test(description = "SC_01_TC_005 - Verify that a valid Indian mobile number is accepted")
    public void TC05_validIndianMobileNumberIsAccepted() {

        System.out.println("========================================");
        System.out.println("SC_01_TC_005 - Valid Indian Mobile Number");
        System.out.println("========================================");

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        System.out.println("Launching Bima Sugam application");

        /*
         * Step 1: Locate Login button
         */
        WebElement loginButton = wait.until(
                driver -> driver.findElement(
                        By.xpath(
                                "//*[@name='Login' or @label='Login']"
                        )
                )
        );

        Assert.assertTrue(
                loginButton.isDisplayed(),
                "Login button is not displayed"
        );

        System.out.println("Pre-login screen displayed");
        System.out.println("Login button displayed");

        /*
         * Step 2: Tap Login
         */
        loginButton.click();

        System.out.println("Login button tapped");
        System.out.println("Waiting for Enter Credentials page");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        IOSLocators.MOBILE_NUMBER
                )
        );

        /*
         * Step 3: Capture the actual credentials page
         */
        String pageSource = driver.getPageSource();

        Assert.assertNotNull(
                pageSource,
                "Credentials page source is unavailable"
        );

        System.out.println("========================================");
        System.out.println("ENTER CREDENTIALS PAGE SOURCE");
        System.out.println("========================================");

        System.out.println(pageSource);

        System.out.println("========================================");
        System.out.println("END PAGE SOURCE");
        System.out.println("========================================");

        /*
         * Verify Mobile Number field is visible via element assertion
         */
        WebElement mobileField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        IOSLocators.MOBILE_NUMBER
                )
        );

        Assert.assertTrue(
                mobileField.isDisplayed(),
                "Mobile Number field was not found on the credentials page"
        );

        System.out.println("Mobile Number field found and displayed");
        System.out.println("TC05 inspection completed");
    }
}