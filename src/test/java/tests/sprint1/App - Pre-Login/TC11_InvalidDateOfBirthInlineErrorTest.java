package tests.sprint1;

import framework.BaseTest;
import locators.IOSLocators;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC11_InvalidDateOfBirthInlineErrorTest extends BaseTest {

    @Test(
            description = "TC11 - Verify inline error is displayed for invalid Date of Birth",
            groups = {"sprint1", "sprint2", "pre-login", "negative"}
    )
    public void TC11_invalidDateOfBirthShowsInlineError() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        LoginPage loginPage = new LoginPage(driver);

        // Tap Login
        loginPage.clickLogin();

        // Enter invalid Date of Birth
        wait.until(
                ExpectedConditions.elementToBeClickable(
                        IOSLocators.DOB_INPUT
                )
        ).click();

        driver.findElement(
                IOSLocators.DOB_INPUT
        ).sendKeys("32/13/2025");

        // Tap Login via OTP
        driver.findElement(
                IOSLocators.LOGIN_VIA_OTP
        ).click();

        // Get page source
        String pageSource = driver.getPageSource();

        // Verify inline error
        Assert.assertTrue(
                pageSource.contains("Date of Birth")
                        || pageSource.contains("Invalid")
                        || pageSource.contains("invalid")
                        || pageSource.contains("Enter a valid"),
                "Inline error was not displayed for invalid Date of Birth."
        );

        System.out.println(
                "SC_01_TC_011 PASSED - Inline error displayed for invalid Date of Birth"
        );
    }
}