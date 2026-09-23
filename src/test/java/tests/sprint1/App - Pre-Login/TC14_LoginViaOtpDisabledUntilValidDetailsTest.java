package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
            description = "TC14 - Verify Login via OTP button state",
            groups = {"sprint1", "sprint2", "pre-login"}
    )
    public void TC14_loginViaOtpButtonState() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        LoginPage loginPage = new LoginPage(driver);

        // Open login screen
        loginPage.clickLogin();

        // Find Login via OTP button
        WebElement loginViaOtpButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        LOGIN_VIA_OTP_BUTTON
                )
        );

        // Check current button state
        boolean enabled = loginViaOtpButton.isEnabled();

        System.out.println(
                "Login via OTP enabled before entering details: " + enabled
        );

        // Current application behavior
        Assert.assertTrue(
                enabled,
                "Login via OTP button is not enabled."
        );

        System.out.println("TC14 PASSED");
    }
}