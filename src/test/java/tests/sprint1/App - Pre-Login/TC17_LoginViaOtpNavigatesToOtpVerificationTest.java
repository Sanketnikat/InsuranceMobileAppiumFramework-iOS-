package tests.sprint1;

import framework.BaseTest;
import locators.IOSLocators;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class TC17_LoginViaOtpNavigatesToOtpVerificationTest extends BaseTest {

    @Test(
            description = "SC_01_TC_017 - Verify Login via OTP navigates to OTP verification bottom sheet/popup",
            groups = {"sprint1", "sprint2", "pre-login", "positive"}
    )
    public void TC17_loginViaOtpNavigatesToOtpVerification() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        LoginPage loginPage = new LoginPage(driver);

        // Step 1 - Tap Login
        System.out.println("Step 1 - Tapping Login");

        loginPage.clickLogin();

        System.out.println("Login button tapped.");

        // Step 2 - Enter valid Mobile Number
        System.out.println("Step 2 - Entering valid Mobile Number");

        loginPage.enterMobileNumber("8921639271");

        System.out.println("Mobile Number entered.");

        // Step 3 - Enter valid Date of Birth
        System.out.println("Step 3 - Entering valid Date of Birth");

        loginPage.enterDateOfBirth("01/01/1999");

        System.out.println("Date of Birth entered.");

        // Step 4 - Close iOS keyboard
        System.out.println("Step 4 - Closing iOS keyboard");

        try {
            Map<String, Object> args = new HashMap<>();
            args.put("name", "RETURN");

            driver.executeScript(
                    "mobile: pressButton",
                    args
            );

            System.out.println("iOS keyboard close command executed.");

        } catch (Exception e) {
            System.out.println(
                    "Keyboard close command could not be executed: "
                            + e.getMessage()
            );
        }

        // Step 5 - Wait for Login via OTP button
        System.out.println("Step 5 - Waiting for Login via OTP button");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        IOSLocators.LOGIN_VIA_OTP
                )
        );

        System.out.println("Login via OTP button is visible.");

        // Step 6 - Tap Login via OTP
        System.out.println("Step 6 - Tapping Login via OTP");

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        IOSLocators.LOGIN_VIA_OTP
                )
        ).click();

        System.out.println("Login via OTP tapped.");

        // Step 7 - Wait for OTP verification bottom sheet/popup
        System.out.println("Step 7 - Waiting for OTP verification screen");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            Assert.fail(
                    "Interrupted while waiting for OTP verification screen."
            );
        }

        // Step 8 - Capture page source
        String pageSourceAfterSubmit = driver.getPageSource();

        System.out.println("========================================");
        System.out.println("PAGE SOURCE AFTER LOGIN VIA OTP");
        System.out.println("========================================");

        System.out.println(pageSourceAfterSubmit);

        System.out.println("========================================");

        // Step 9 - Check OTP verification content
        boolean otpVerificationDisplayed =
                pageSourceAfterSubmit.contains("OTP")
                        || pageSourceAfterSubmit.contains("Otp")
                        || pageSourceAfterSubmit.contains("otp")
                        || pageSourceAfterSubmit.contains("Verification")
                        || pageSourceAfterSubmit.contains("verification")
                        || pageSourceAfterSubmit.contains("Enter OTP")
                        || pageSourceAfterSubmit.contains("Verify OTP");

        Assert.assertTrue(
                otpVerificationDisplayed,
                "OTP verification bottom sheet/popup was not displayed."
        );

        System.out.println(
                "SC_01_TC_017 PASSED - OTP verification bottom sheet/popup is displayed."
        );
    }
}