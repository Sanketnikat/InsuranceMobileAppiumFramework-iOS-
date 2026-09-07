package tests.sprint1;

import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class LoginPage {
    private final AndroidDriver driver;

    /*
     * PLACEHOLDER LOCATORS
     * Replace these with the actual locators from your application.
     */
    private final By mobileNumber = AppiumBy.accessibilityId("mobileNumber");
    private final By continueButton = AppiumBy.accessibilityId("continue");
    private final By otp = AppiumBy.accessibilityId("otp");
    private final By loginButton = AppiumBy.accessibilityId("login");

    public LoginPage(AndroidDriver driver) {
        this.driver = driver;
    }

    public LoginPage enterMobileNumber(String value) {
        WaitUtils.visible(driver, mobileNumber);
        driver.findElement(mobileNumber).clear();
        driver.findElement(mobileNumber).sendKeys(value);
        return this;
    }

    public LoginPage clickContinue() {
        WaitUtils.clickable(driver, continueButton);
        driver.findElement(continueButton).click();
        return this;
    }

    public LoginPage enterOtp(String value) {
        WaitUtils.visible(driver, otp);
        driver.findElement(otp).clear();
        driver.findElement(otp).sendKeys(value);
        return this;
    }

    public LoginPage clickLogin() {
        WaitUtils.clickable(driver, loginButton);
        driver.findElement(loginButton).click();
        return this;
    }
}
