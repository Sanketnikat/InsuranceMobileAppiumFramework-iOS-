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

public class TC20_ErrorMessageWhenOTPSendFailsDueToRateLimitTest extends BaseTest {

    @Test(
            description = "SC_01_TC_020 - Verify appropriate error message is displayed when OTP send fails due to rate limit",
            groups = {"sprint1", "sprint2", "pre-login", "negative"}
    )
    public void TC20_errorMessageWhenOtpSendFailsDueToRateLimit() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        LoginPage loginPage = new LoginPage(driver);

        String mobileNumber = "8921639271";

        // Step 1 - Tap Login
        System.out.println("Step 1 - Tapping Login");

        loginPage.clickLogin();

        System.out.println("Login button tapped.");

        // Step 2 - Enter Mobile Number
        System.out.println("Step 2 - Entering Mobile Number");

        loginPage.enterMobileNumber(mobileNumber);

        System.out.println("Mobile Number entered.");

        // Step 3 - Enter Date of Birth
        System.out.println("Step 3 - Entering Date of Birth");

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

        // Step 5 - Wait for Login via OTP
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

        // Step 7 - Wait for response
        System.out.println("Step 7 - Waiting for OTP send response");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            Assert.fail(
                    "Interrupted while waiting for OTP send response."
            );
        }

        // Step 8 - Capture page source
        String pageSource = driver.getPageSource();

        System.out.println("========================================");
        System.out.println("PAGE SOURCE AFTER OTP REQUEST");
        System.out.println("========================================");

        System.out.println(pageSource);

        System.out.println("========================================");

        // Step 9 - Check for rate-limit/error message
        boolean errorMessageDisplayed =
                pageSource.contains("rate limit")
                        || pageSource.contains("Rate limit")
                        || pageSource.contains("too many")
                        || pageSource.contains("Too many")
                        || pageSource.contains("try again")
                        || pageSource.contains("Try again")
                        || pageSource.contains("maximum")
                        || pageSource.contains("Maximum")
                        || pageSource.contains("failed")
                        || pageSource.contains("Failed");

        Assert.assertTrue(
                errorMessageDisplayed,
                "Appropriate error message for OTP rate limit was not displayed."
        );

        System.out.println(
                "Appropriate OTP rate-limit error message is displayed."
        );

        System.out.println(
                "SC_01_TC_020 PASSED - Rate-limit error message displayed."
        );
    }
}