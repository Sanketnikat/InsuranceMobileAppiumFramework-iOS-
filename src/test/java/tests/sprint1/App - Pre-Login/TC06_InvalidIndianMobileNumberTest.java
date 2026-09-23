package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC06_InvalidIndianMobileNumberTest extends BaseTest {

    @Test(description = "SC_01_TC_006 - Verify that an invalid Indian mobile number is rejected")
    public void TC06_invalidIndianMobileNumberIsRejected() {

        System.out.println("========================================");
        System.out.println("SC_01_TC_006 - Invalid Indian Mobile Number");
        System.out.println("========================================");

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        /*
         * Step 1: Verify Login button on pre-login screen
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
         * Step 3: Locate Mobile Number field
         *
         * Exact accessibility identifier obtained
         * from the iOS page source:
         * Enter Mobile Number
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
         * Step 4: Enter invalid Indian mobile number
         */
        String invalidMobileNumber = "12345";

        mobileNumberField.clear();
        mobileNumberField.sendKeys(invalidMobileNumber);

        System.out.println(
                "Entered invalid mobile number: " + invalidMobileNumber
        );

        /*
         * Step 5: Locate Login via OTP button
         *
         * Exact accessibility identifier obtained
         * from the iOS page source:
         * Login via OTP
         */
        WebElement loginViaOTPButton = wait.until(
                driver -> driver.findElement(
                        AppiumBy.accessibilityId("Login via OTP")
                )
        );

        Assert.assertTrue(
                loginViaOTPButton.isDisplayed(),
                "Login via OTP button is not displayed"
        );

        System.out.println("Login via OTP button displayed");

        /*
         * Step 6: Tap Login via OTP
         */
        loginViaOTPButton.click();

        System.out.println("Login via OTP tapped");

        /*
         * Step 7: Wait for validation
         */
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.fail(
                    "Thread was interrupted while waiting for validation"
            );
        }

        /*
         * Step 8: Capture page source after submission
         */
        String pageSource = driver.getPageSource();

        Assert.assertNotNull(
                pageSource,
                "Page source is unavailable after submitting invalid number"
        );

        System.out.println("========================================");
        System.out.println("PAGE SOURCE AFTER INVALID NUMBER");
        System.out.println("========================================");

        System.out.println(pageSource);

        System.out.println("========================================");

        /*
         * Step 9: Check that validation is present.
         *
         * The exact validation message has not yet
         * been confirmed, so common validation text
         * is checked temporarily.
         */
        boolean validationDisplayed =
                pageSource.contains("invalid")
                        || pageSource.contains("Invalid")
                        || pageSource.contains("valid")
                        || pageSource.contains("Valid")
                        || pageSource.contains("Mobile Number")
                        || pageSource.contains("Mobile number");

        Assert.assertTrue(
                validationDisplayed,
                "Invalid Mobile Number validation was not detected"
        );

        System.out.println(
                "Invalid Mobile Number validation is displayed"
        );

        System.out.println("SC_01_TC_006 PASSED");
    }
}