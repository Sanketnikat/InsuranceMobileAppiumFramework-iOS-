package tests.sprint1;

import framework.BaseTest;
import locators.IOSLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC10_DateOfBirthFieldOpensDatePickerTest extends BaseTest {

    private static final By DATE_PICKER =
            io.appium.java_client.AppiumBy.iOSClassChain(
                    "**/XCUIElementTypeDatePicker"
            );

    @Test(
            description = "TC10 - Verify DOB / Date of Incorporation opens a date picker",
            groups = {"sprint1", "sprint2", "pre-login", "positive"}
    )
    public void TC10_dateOfBirthFieldOpensDatePicker() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        LoginPage loginPage = new LoginPage(driver);

        // Tap Login
        loginPage.clickLogin();

        // Tap Date of Birth
        wait.until(
                ExpectedConditions.elementToBeClickable(
                        IOSLocators.DOB_INPUT
                )
        ).click();

        // Verify Date Picker is displayed
        boolean pickerVisible = wait.until(driver -> {
            try {
                return driver.findElement(DATE_PICKER).isDisplayed();
            } catch (Exception e) {
                return false;
            }
        });

        Assert.assertTrue(
                pickerVisible,
                "Date picker did not open for Date of Birth / Date of Incorporation."
        );

        System.out.println("TC10 PASSED - Date Picker opened successfully");
    }
}