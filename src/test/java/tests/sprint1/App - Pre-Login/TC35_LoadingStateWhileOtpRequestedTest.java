package tests.sprint1;

import framework.BaseTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.OutputType;

import io.appium.java_client.AppiumBy;

import java.time.Duration;

public class TC35_LoadingStateWhileOtpRequestedTest extends BaseTest {

    @Test
    public void TC35_verifyLoadingStateWhileOtpRequested() {

        System.out.println("================================");
        System.out.println("TC35 - Loading State While OTP Requested");
        System.out.println("================================");

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        // Step 1 - Tap Login
        driver.findElement(
                AppiumBy.xpath("//*[@name='Login' or @label='Login']")
        ).click();

        System.out.println("Login button tapped.");

        // Step 2 - Enter Mobile Number
        driver.findElement(
                AppiumBy.accessibilityId("Enter Mobile Number")
        ).sendKeys("8921639271");

        System.out.println("Mobile Number entered.");

        // Step 3 - Enter Date of Birth
        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterDateOfBirth("01/01/1999");

        System.out.println("Date of Birth entered.");

        // Step 4 - Check actual DOB value
        WebElement dobField = driver.findElement(
                AppiumBy.xpath(
                        "//XCUIElementTypeTextField[@value='01/01/1999']"
                )
        );

        String actualDob = dobField.getAttribute("value");

        System.out.println("================================");
        System.out.println("DOB VALUE ENTERED IN APP");
        System.out.println("================================");
        System.out.println(actualDob);
        System.out.println("================================");

        Assert.assertEquals(
                actualDob,
                "01/01/1999",
                "DOB was entered incorrectly."
        );

        System.out.println(
                "DOB value is correct: " + actualDob
        );

        // Step 5 - Tap Login via OTP
        WebElement loginViaOtpButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        AppiumBy.accessibilityId("Login via OTP")
                )
        );

        loginViaOtpButton.click();

        System.out.println("Login via OTP tapped.");

        // Step 6 - Immediately capture screenshot
        try {

            File screenshot = driver.getScreenshotAs(
                    OutputType.FILE
            );

            File screenshotFolder = new File(
                    "screenshots"
            );

            if (!screenshotFolder.exists()) {
                screenshotFolder.mkdirs();
            }

            File destination = new File(
                    screenshotFolder,
                    "TC35_LoadingState.png"
            );

            Files.copy(
                    screenshot.toPath(),
                    destination.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println(
                    "TC35 screenshot saved: "
                            + destination.getAbsolutePath()
            );

        } catch (Exception e) {

            System.out.println(
                    "Screenshot could not be saved: "
                            + e.getMessage()
            );
        }

        // Step 7 - Check page source
        String pageSource = driver.getPageSource();

        System.out.println("================================");
        System.out.println("PAGE SOURCE AFTER OTP REQUEST");
        System.out.println("================================");
        System.out.println(pageSource);
        System.out.println("================================");

        // Step 8 - Check loading state
        boolean loadingDisplayed =
                pageSource.contains("Loading")
                        || pageSource.contains("loading")
                        || pageSource.contains("Progress")
                        || pageSource.contains("progress");

        if (loadingDisplayed) {

            System.out.println(
                    "Loading state detected."
            );

            System.out.println(
                    "SC_01_TC_035 PASSED - Loading state is displayed."
            );

        } else {

            System.out.println(
                    "Loading state was not detected in page source."
            );

            System.out.println(
                    "Screenshot has been saved for visual verification."
            );

            Assert.fail(
                    "Loading state was not detected while OTP was being requested."
            );
        }
    }
}