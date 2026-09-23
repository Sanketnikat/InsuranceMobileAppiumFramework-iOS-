package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC40_MultipleLoginViaOtpTapTest extends BaseTest {

    @Test
    public void TC40_verifyMultipleLoginViaOtpTap() {

        System.out.println("======================================");
        System.out.println("TC40 - MULTIPLE LOGIN VIA OTP TAP");
        System.out.println("======================================");

        // Step 1 - Click Login button
        WebElement loginButton = driver.findElement(
                AppiumBy.xpath("//*[@name='Login' or @label='Login']")
        );

        loginButton.click();

        System.out.println("Login button clicked.");

        // Step 2 - Enter Mobile Number
        WebElement mobileNumber = driver.findElement(
                AppiumBy.accessibilityId("Enter Mobile Number")
        );

        mobileNumber.click();
        mobileNumber.sendKeys("7263935191");

        System.out.println("Mobile number entered.");

        // Step 3 - Enter Date of Birth
        WebElement dateOfBirth = driver.findElement(
                AppiumBy.accessibilityId("Date of Birth")
        );

        dateOfBirth.click();
        dateOfBirth.sendKeys("04052000");

        System.out.println("Date of Birth entered: 04/05/2000");

        // Wait for DOB value to be processed
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Step 4 - Close keyboard by tapping outside the keyboard
        System.out.println("Closing keyboard by tapping outside the keyboard...");

        try {
            driver.executeScript(
                    "mobile: tap",
                    java.util.Map.of(
                            "x", 200,
                            "y", 120
                    )
            );

            Thread.sleep(1000);

            System.out.println("Keyboard close tap performed.");

        } catch (Exception e) {

            System.out.println(
                    "Coordinate tap failed: " + e.getMessage()
            );

            Assert.fail(
                    "Could not close keyboard before tapping Login via OTP."
            );
        }

        // Step 5 - Find Login via OTP button
        WebElement loginViaOTP = driver.findElement(
                AppiumBy.accessibilityId("Login via OTP")
        );

        Assert.assertTrue(
                loginViaOTP.isDisplayed(),
                "Login via OTP button is not displayed after closing keyboard."
        );

        System.out.println("Login via OTP button is displayed.");

        // Step 6 - Verify button is enabled
        Assert.assertTrue(
                loginViaOTP.isEnabled(),
                "Login via OTP button is not enabled."
        );

        System.out.println("Login via OTP button is enabled.");

        // Step 7 - Tap Login via OTP multiple times
        System.out.println("Tapping Login via OTP multiple times...");

        loginViaOTP.click();

        System.out.println("First tap completed.");

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        loginViaOTP = driver.findElement(
                AppiumBy.accessibilityId("Login via OTP")
        );

        loginViaOTP.click();

        System.out.println("Second tap completed.");

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        loginViaOTP = driver.findElement(
                AppiumBy.accessibilityId("Login via OTP")
        );

        loginViaOTP.click();

        System.out.println("Third tap completed.");

        // Step 8 - Wait for OTP verification screen
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String pageSource = driver.getPageSource();

        System.out.println("======================================");
        System.out.println("PAGE SOURCE AFTER MULTIPLE TAPS");
        System.out.println("======================================");

        System.out.println(pageSource);

        // Step 9 - Verify OTP verification window
        Assert.assertTrue(
                pageSource.contains("OTP Verification")
                        || pageSource.contains("Verify your Mobile Number"),
                "OTP verification window was not displayed after multiple taps."
        );

        System.out.println("======================================");
        System.out.println("TC40 PASSED");
        System.out.println("OTP verification window displayed.");
        System.out.println("======================================");
    }
}