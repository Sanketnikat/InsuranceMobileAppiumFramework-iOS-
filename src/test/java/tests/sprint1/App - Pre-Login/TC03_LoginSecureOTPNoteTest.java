package tests.sprint1;

import framework.BaseTest;
import locators.IOSLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC03_LoginSecureOTPNoteTest extends BaseTest {

    @Test(description = "SC_01_TC_003 - Verify that the login card displays the secure OTP note")
    public void verifySecureOTPNoteDisplayed() {

        System.out.println("========================================");
        System.out.println("SC_01_TC_003 - Secure OTP Note");
        System.out.println("========================================");

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        System.out.println("Launching Bima Sugam application");

        /*
         * Step 1: Verify Login button is available
         */
        WebElement loginButton = wait.until(
                driver -> driver.findElement(
                        By.xpath(
                                "//*[@name='Login' or @label='Login']"
                        )
                )
        );

        Assert.assertTrue(
                loginButton.isDisplayed(),
                "Login button is not displayed on the pre-login screen"
        );

        System.out.println("Pre-login screen displayed");
        System.out.println("Login button is displayed");

        /*
         * Step 2: Tap Login
         */
        loginButton.click();

        System.out.println("Login button tapped");
        System.out.println("Waiting for Enter Credentials page");

        /*
         * Step 3: Wait for the Enter Credentials page
         */
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        IOSLocators.MOBILE_NUMBER
                )
        );

        /*
         * Step 4: Capture the current page source
         */
        String pageSource = driver.getPageSource();

        Assert.assertNotNull(
                pageSource,
                "Enter Credentials page UI is unavailable"
        );

        System.out.println("Enter Credentials page loaded");

        System.out.println("========== ENTER CREDENTIALS PAGE SOURCE ==========");
        System.out.println(pageSource);
        System.out.println("========== END PAGE SOURCE ==========");

        /*
         * Step 5: Verify Secure OTP note via element assertion
         */
        WebElement otpNote = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        IOSLocators.OTP_HINT
                )
        );

        Assert.assertTrue(
                otpNote.isDisplayed(),
                "The note 'Your details will be verified by a secure OTP' is not displayed on the Enter Credentials page"
        );

        System.out.println(
                "Your details will be verified by a secure OTP - displayed"
        );

        System.out.println("SC_01_TC_003 PASSED");
    }
}