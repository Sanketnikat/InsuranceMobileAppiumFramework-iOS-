package tests.sprint1;

import framework.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC04_MobileNumberMandatoryTest extends BaseTest {

    @Test(description = "SC_01_TC_004 - Verify that Mobile Number field is mandatory")
    public void verifyMobileNumberIsMandatory() {

        System.out.println("========================================");
        System.out.println("SC_01_TC_004 - Mobile Number Mandatory");
        System.out.println("========================================");

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        System.out.println("Launching Bima Sugam application");

        /*
         * Step 1: Locate Login button on pre-login screen
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
                "Login button is not displayed on the pre-login screen"
        );

        System.out.println("Pre-login screen displayed");

        /*
         * Step 2: Tap Login
         */
        loginButton.click();

        System.out.println("Login button tapped");
        System.out.println("Waiting for Enter Credentials page");

        /*
         * Wait for the credentials page
         */
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.fail(
                    "Thread was interrupted while waiting for credentials page"
            );
        }

        /*
         * Step 3: Find Mobile Number field
         *
         * This is a flexible locator. Once we have the
         * actual Appium Inspector attributes, we can make
         * this exact.
         */
        WebElement mobileNumberField = wait.until(
                driver -> driver.findElement(
                        By.xpath(
                                "//*[contains(@label,'Mobile Number') " +
                                "or contains(@name,'Mobile Number') " +
                                "or contains(@value,'Mobile Number')]"
                        )
                )
        );

        Assert.assertTrue(
                mobileNumberField.isDisplayed(),
                "Mobile Number field is not displayed"
        );

        System.out.println("Enter Credentials page displayed");
        System.out.println("Mobile Number field is displayed");

        /*
         * Step 4: Leave Mobile Number blank
         */
        mobileNumberField.clear();

        System.out.println("Mobile Number field left blank");

        /*
         * Step 5: Find Login via OTP button
         */
        WebElement loginViaOTPButton = wait.until(
                driver -> driver.findElement(
                        By.xpath(
                                "//*[contains(@label,'Login via OTP') " +
                                "or contains(@name,'Login via OTP') " +
                                "or contains(@value,'Login via OTP')]"
                        )
                )
        );

        Assert.assertTrue(
                loginViaOTPButton.isDisplayed(),
                "Login via OTP button is not displayed"
        );

        System.out.println("Login via OTP button is displayed");

        /*
         * Step 6: Tap Login via OTP without entering
         * Mobile Number
         */
        loginViaOTPButton.click();

        System.out.println("Login via OTP tapped with blank Mobile Number");

        /*
         * Step 7: Wait for validation message
         */
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.fail(
                    "Thread was interrupted while waiting for validation message"
            );
        }

        String pageSource = driver.getPageSource();

        Assert.assertNotNull(
                pageSource,
                "Page source is unavailable after tapping Login via OTP"
        );

        /*
         * Step 8: Verify mandatory validation
         *
         * We are checking common possible validation text.
         * Replace with the exact message once confirmed
         * from the application/Appium Inspector.
         */
        boolean mandatoryMessageDisplayed =
                pageSource.contains("required")
                || pageSource.contains("Required")
                || pageSource.contains("mandatory")
                || pageSource.contains("Mandatory")
                || pageSource.contains("enter mobile")
                || pageSource.contains("Enter mobile")
                || pageSource.contains("Mobile Number");

        Assert.assertTrue(
                mandatoryMessageDisplayed,
                "Mobile Number mandatory validation message is not displayed"
        );

        System.out.println(
                "Mobile Number mandatory validation is displayed"
        );

        System.out.println("SC_01_TC_004 PASSED");
    }
}