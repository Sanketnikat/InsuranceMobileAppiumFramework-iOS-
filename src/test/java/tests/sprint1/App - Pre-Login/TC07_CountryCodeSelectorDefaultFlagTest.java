package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC07_CountryCodeSelectorDefaultFlagTest extends BaseTest {

    @Test(description = "SC_01_TC_007 - Verify country code selector is displayed with default flag")
    public void verifyCountryCodeSelectorDefaultFlag() {

        System.out.println("========================================");
        System.out.println("SC_01_TC_007 - Country Code Selector");
        System.out.println("========================================");

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        /*
         * Step 1: Locate Login button on pre-login screen
         */
        WebElement loginButton = wait.until(
                driver -> driver.findElement(
                        By.xpath("//*[@name='Login' or @label='Login']")
                )
        );

        Assert.assertTrue(
                loginButton.isDisplayed(),
                "Login button is not displayed"
        );

        System.out.println("Pre-login screen displayed");

        /*
         * Step 2: Tap Login
         */
        loginButton.click();

        System.out.println("Login button tapped");

        /*
         * Step 3: Wait for Mobile Number field
         */
        WebElement mobileNumberField = wait.until(
                driver -> driver.findElement(
                        AppiumBy.accessibilityId("Enter Mobile Number")
                )
        );

        Assert.assertTrue(
                mobileNumberField.isDisplayed(),
                "Mobile Number field is not displayed"
        );

        System.out.println("Enter Credentials page displayed");
        System.out.println("Mobile Number field displayed");

        /*
         * Step 4: Locate the country code / flag element.
         *
         * The page source shows an image at:
         *
         * x=57, y=395
         * width=24, height=16
         *
         * immediately before the Mobile Number text field.
         *
         * Since this image currently has no name/label,
         * we locate it relative to the Mobile Number field.
         */
        WebElement countryFlag = wait.until(
                driver -> driver.findElement(
                        By.xpath(
                                "//*[@name='Enter Mobile Number']" +
                                "/preceding-sibling::XCUIElementTypeImage"
                        )
                )
        );

        Assert.assertTrue(
                countryFlag.isDisplayed(),
                "Default country flag is not displayed"
        );

        System.out.println("Country code selector/default flag is displayed");
        System.out.println("Default flag is displayed on the Mobile Number field");

        System.out.println("SC_01_TC_007 PASSED");
    }
}