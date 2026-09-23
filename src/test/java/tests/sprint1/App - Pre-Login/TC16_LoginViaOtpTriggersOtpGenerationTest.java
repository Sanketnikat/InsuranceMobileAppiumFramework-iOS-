package tests.sprint1;

import framework.BaseTest;
import locators.IOSLocators;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
public class TC16_LoginViaOtpTriggersOtpGenerationTest extends BaseTest {

    @Test(
            description = "TC16 - Verify Login via OTP submits details and triggers OTP generation",
            groups = {"sprint1", "sprint2", "pre-login", "positive"}
    )
    public void TC16_loginViaOtpTriggersOtpGeneration() {

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

                // Step 4 - Close iOS Keyboard
        System.out.println("Step 4 - Closing iOS keyboard");

                loginPage.dismissKeyboard();
                System.out.println("iOS keyboard close command executed.");

        // Step 5 - Wait for Login via OTP button
        System.out.println("Step 5 - Waiting for Login via OTP button");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        IOSLocators.LOGIN_VIA_OTP
                )
        );

        System.out.println("Login via OTP button is visible.");

        // Step 6 - Print page source before submission
        System.out.println("========================================");
        System.out.println("PAGE SOURCE BEFORE LOGIN VIA OTP");
        System.out.println("========================================");

        String pageSourceBeforeSubmit = driver.getPageSource();

        System.out.println(pageSourceBeforeSubmit);

        System.out.println("========================================");

        // Step 7 - Verify Login via OTP button
        Assert.assertTrue(
                pageSourceBeforeSubmit.contains("Login via OTP"),
                "Login via OTP button is not displayed."
        );

        System.out.println("Login via OTP button is displayed.");

        // Step 8 - Tap Login via OTP
        System.out.println("Step 6 - Tapping Login via OTP");

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        IOSLocators.LOGIN_VIA_OTP
                )
        ).click();

        System.out.println("Login via OTP tapped successfully.");

        // Step 9 - Wait for OTP screen to appear
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        IOSLocators.OTP_VERIFICATION
                )
        );

        // Step 10 - Capture page source after submission
        String pageSourceAfterSubmit = driver.getPageSource();

        System.out.println("========================================");
        System.out.println("PAGE SOURCE AFTER LOGIN VIA OTP");
        System.out.println("========================================");

        System.out.println(pageSourceAfterSubmit);

        System.out.println("========================================");

        // Step 11 - Verify OTP screen is now displayed
        Assert.assertTrue(
                !driver.findElements(IOSLocators.OTP_VERIFICATION).isEmpty(),
                "Login via OTP did not navigate to OTP Verification screen."
        );

        System.out.println(
                "SC_01_TC_016 PASSED - Login via OTP submitted successfully."
        );
    }
}