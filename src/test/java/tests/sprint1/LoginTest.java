package tests.sprint1;

import framework.BaseTest;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "TC01 - Valid login")
    public void TC01_validLogin() {
        LoginPage loginPage = new LoginPage(driver);

        step("Enter valid mobile number");
        loginPage.enterMobileNumber("9999999999");

        step("Click Continue");
        loginPage.clickContinue();

        step("Enter valid OTP");
        loginPage.enterOtp("123456");

        step("Click Login");
        loginPage.clickLogin();

        // Add the real success assertion when the application flow is known.
    }

    @Test(description = "TC02 - Invalid mobile number")
    public void TC02_invalidMobileNumber() {
        LoginPage loginPage = new LoginPage(driver);

        step("Enter invalid mobile number");
        loginPage.enterMobileNumber("123");

        step("Click Continue");
        loginPage.clickContinue();

        // Add the real validation assertion when the UI is known.
    }

    /*
     * TC03 - TC20 will be added here after the actual Sprint 1
     * test cases are available.
     */
}
