package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC19_VerifyErrorMessageWhenOTPSendFailsForInvalidNumber
        extends BaseTest {

    @Test(
            description = "TC19 - Verify error message when OTP send fails for invalid number",
            groups = {"sprint1", "login"}
    )
    public void TC19_VerifyErrorMessageWhenOTPSendFailsForInvalidNumber() {

        LoginPage loginPage = new LoginPage(driver);

        step("Tap Login");
        loginPage.clickLogin();

        step("Enter invalid mobile number");
        loginPage.enterMobileNumber("1234567890");

        step("Enter date of birth");
        loginPage.enterDateOfBirth("01/01/1999");

        step("Tap Login via OTP");
        loginPage.clickLoginViaOtp();

        step("Verify error message");

        boolean errorDisplayed = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        ).until(
                ExpectedConditions.visibilityOfElementLocated(
                        AppiumBy.iOSClassChain(
                                "**/XCUIElementTypeAlert"
                        )
                )
        ).isDisplayed();

        Assert.assertTrue(
                errorDisplayed,
                "Error message should be displayed for invalid mobile number."
        );
    }
}