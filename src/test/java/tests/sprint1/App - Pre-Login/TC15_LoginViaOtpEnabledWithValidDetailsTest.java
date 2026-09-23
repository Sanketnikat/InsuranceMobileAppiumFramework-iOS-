package tests.sprint1;

import framework.BaseTest;
import locators.IOSLocators;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC15_LoginViaOtpEnabledWithValidDetailsTest extends BaseTest {

    @Test(
            description = "TC15 - Verify Login via OTP is enabled when valid Mobile Number and Date of Birth are entered",
            groups = {"sprint1", "sprint2", "pre-login", "positive"}
    )
    public void TC15_loginViaOtpEnabledWithValidDetails() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        LoginPage loginPage = new LoginPage(driver);

        // Tap Login
        loginPage.clickLogin();

        // Enter valid Mobile Number
        loginPage.enterMobileNumber("8921639271");

        // Enter valid Date of Birth
        loginPage.enterDateOfBirth("01/01/1999");

        // Wait for Login via OTP button
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        IOSLocators.LOGIN_VIA_OTP
                )
        );

        // Verify Login via OTP is enabled
        boolean enabled = driver.findElement(
                IOSLocators.LOGIN_VIA_OTP
        ).isEnabled();

        Assert.assertTrue(
                enabled,
                "Login via OTP button is not enabled when both details are valid."
        );

        System.out.println(
                "SC_01_TC_015 PASSED - Login via OTP is enabled with valid details"
        );
    }
}