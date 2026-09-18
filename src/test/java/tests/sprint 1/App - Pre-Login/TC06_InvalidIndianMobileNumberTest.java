package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC06_InvalidIndianMobileNumberTest extends BaseTest {

    private static final By LOGIN_VIA_OTP =
            AppiumBy.accessibilityId("Login via OTP");

    @Test(
            description = "BD2M-677 - TC06 - Reject an invalid mobile number",
            groups = {"sprint1", "sprint2", "pre-login", "negative"}
    )
    public void TC06_invalidIndianMobileNumberIsRejected() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );
        LoginPage loginPage = new LoginPage(driver);
        String invalidMobileNumber = "5555555555";

        step("Tap Login to open the pre-login form");
        loginPage.clickLogin();

        step("Enter an invalid mobile number");
        loginPage.enterMobileNumber(invalidMobileNumber);

        step("Verify that the invalid mobile number is rejected");
        boolean loginButtonEnabled = wait.until(
                ExpectedConditions.visibilityOfElementLocated(LOGIN_VIA_OTP)
        ).isEnabled();

        Assert.assertFalse(
                loginButtonEnabled,
                "Login via OTP was enabled for an invalid mobile number."
        );
    }

}
