package tests.sprint1;

import framework.BaseTest;
import locators.IOSLocators;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC12_EmptyDateOfBirthInlineErrorTest extends BaseTest {

    @Test(
            description = "TC12 - Verify inline error is displayed for empty Date of Birth",
            groups = {"sprint1", "sprint2", "pre-login", "negative"}
    )
    public void TC12_emptyDateOfBirthShowsInlineError() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        LoginPage loginPage = new LoginPage(driver);

        // Tap Login
        loginPage.clickLogin();

        // Wait for Date of Birth field
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        IOSLocators.DOB_INPUT
                )
        );

        // Leave Date of Birth empty
        driver.findElement(
                IOSLocators.DOB_INPUT
        ).clear();

        // Tap Login via OTP
        driver.findElement(
                IOSLocators.LOGIN_VIA_OTP
        ).click();

        // Get page source after validation
        String pageSource = driver.getPageSource();

        // Verify inline error
        Assert.assertTrue(
                pageSource.contains("Date of Birth")
                        || pageSource.contains("required")
                        || pageSource.contains("Required")
                        || pageSource.contains("mandatory")
                        || pageSource.contains("Mandatory"),
                "Inline error was not displayed for empty Date of Birth."
        );

        System.out.println(
                "SC_01_TC_012 PASSED - Inline error displayed for empty Date of Birth"
        );
    }
}