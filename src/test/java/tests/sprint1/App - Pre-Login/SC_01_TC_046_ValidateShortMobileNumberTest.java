package tests.sprint1;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SC_01_TC_046_ValidateShortMobileNumberTest extends BaseTest {

    @Test(
            description = "SC_01_TC_046 - Verify mobile number with fewer than 10 digits is rejected and Login via OTP remains disabled",
            groups = {"sprint1", "login"}
    )
    public void SC_01_TC_046_ValidateShortMobileNumber() {

        LoginPage loginPage = new LoginPage(driver);

        System.out.println("======================================");
        System.out.println("SC_01_TC_046 STARTED");
        System.out.println("======================================");

        step("Tap Login");
        loginPage.clickLogin();

        step("Enter mobile number with fewer than 10 digits");
        String shortMobileNumber = "726393519";
        loginPage.enterMobileNumber(shortMobileNumber);

        System.out.println("Entered Mobile Number: " + shortMobileNumber);

        step("Verify mobile number value");
        String enteredMobileNumber = loginPage.getMobileNumberValue();

        System.out.println(
                "Mobile Number field value: [" + enteredMobileNumber + "]"
        );

        Assert.assertEquals(
                enteredMobileNumber,
                shortMobileNumber,
                "Mobile Number field should contain the entered 9-digit number."
        );

        step("Verify inline validation error is displayed");

        boolean errorDisplayed =
                loginPage.isMobileNumberValidationErrorDisplayed();

        System.out.println(
                "Mobile Number validation error displayed: "
                        + errorDisplayed
        );

        Assert.assertTrue(
                errorDisplayed,
                "Inline validation error should be displayed for a mobile number with fewer than 10 digits."
        );

        step("Verify Login via OTP button remains disabled");

        boolean loginViaOtpEnabled =
                loginPage.isLoginViaOtpButtonEnabled();

        System.out.println(
                "Login via OTP button enabled: "
                        + loginViaOtpEnabled
        );

        Assert.assertFalse(
                loginViaOtpEnabled,
                "Login via OTP button should remain disabled for a mobile number with fewer than 10 digits."
        );

        System.out.println("======================================");
        System.out.println("SC_01_TC_046 PASSED");
        System.out.println("9-digit mobile number was rejected.");
        System.out.println("Inline validation error was displayed.");
        System.out.println("Login via OTP button remained disabled.");
        System.out.println("======================================");
    }
    
}