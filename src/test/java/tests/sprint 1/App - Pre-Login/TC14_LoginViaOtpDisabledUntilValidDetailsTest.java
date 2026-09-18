package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC14_LoginViaOtpDisabledUntilValidDetailsTest extends BaseTest {

    private static final By LOGIN_VIA_OTP_BUTTON =
            AppiumBy.xpath(
                    "//XCUIElementTypeButton[" +
                    "@name='Login via OTP' or @label='Login via OTP']"
            );

    @Test(
            description = "TC14 - Keep Login via OTP disabled until mobile number and date of birth are valid",
            groups = {"sprint1", "sprint2", "pre-login", "negative"}
    )
    public void TC14_loginViaOtpRemainsDisabledUntilBothDetailsAreValid() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );
        LoginPage loginPage = new LoginPage(driver);

        step("Open the pre-login screen");
        loginPage.clickLogin();

        step("Verify Login via OTP is disabled before entering details");
        Assert.assertFalse(
                loginViaOtpButton(wait).isEnabled(),
                "Login via OTP was enabled before valid details were entered."
        );

        step("Enter a valid mobile number only");
        loginPage.enterMobileNumber("8921639271");

        step("Verify Login via OTP remains disabled until the date field is valid");
        Assert.assertFalse(
                loginViaOtpButton(wait).isEnabled(),
                "Login via OTP was enabled with only a valid mobile number."
        );

        step("Enter a valid date of birth");
        loginPage.enterDateOfBirth("01/01/1999");

        step("Verify Login via OTP is enabled after both fields become valid");
        Assert.assertTrue(
                loginViaOtpButton(wait).isEnabled(),
                "Login via OTP remained disabled after both fields became valid."
        );
    }

    private org.openqa.selenium.WebElement loginViaOtpButton(WebDriverWait wait) {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        LOGIN_VIA_OTP_BUTTON
                )
        );
    }
}
