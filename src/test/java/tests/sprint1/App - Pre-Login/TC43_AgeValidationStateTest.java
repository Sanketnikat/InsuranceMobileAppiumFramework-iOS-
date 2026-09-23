package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import locators.IOSLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TC43_AgeValidationStateTest extends BaseTest {

    private static final String AGE_VALIDATION_MESSAGE =
            "You must be at least 18 years old";

    /*
     * Actual top-right button/control found from the iOS page source.
     *
     * x = 342
     * y = 55
     * width = 32
     * height = 34
     *
     * It is XCUIElementTypeOther and does not have a name/label.
     */
    private static final By BIKE_SCREEN_TOP_RIGHT_BUTTON =
            AppiumBy.xpath(
                    "//XCUIElementTypeOther[" +
                    "@x='342' and @y='55' and " +
                    "@width='32' and @height='34'" +
                    "]"
            );

    @Test(
            description = "TC44 - Verify age validation message remains after returning from Private Bike Insurance",
            groups = {"sprint1", "sprint2", "pre-login"}
    )
    public void TC44_ageValidationMessagePersistsAfterCancel() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        LoginPage loginPage = new LoginPage(driver);

        // =========================================
        // STEP 1 - Open Login page
        // =========================================

        step("Open the Login page");

        loginPage.clickLogin();

        // =========================================
        // STEP 2 - Select today's date
        // =========================================

        step("Select today's date in Date of Birth field");

        String todayDate = LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );

        System.out.println(
                "Today's date: " + todayDate
        );

        loginPage.enterDateOfBirth(todayDate);

        // =========================================
        // STEP 3 - Verify age validation message
        // =========================================

        step("Verify age validation message is displayed");

        boolean ageMessageDisplayed = wait.until(
                driver -> driver.getPageSource().contains(
                        AGE_VALIDATION_MESSAGE
                )
        );

        Assert.assertTrue(
                ageMessageDisplayed,
                "Age validation message was not displayed."
        );

        System.out.println("======================================");
        System.out.println("AGE VALIDATION MESSAGE DISPLAYED");
        System.out.println("======================================");
        System.out.println(AGE_VALIDATION_MESSAGE);
        System.out.println("======================================");

        // =========================================
        // STEP 4 - Close keyboard
        // =========================================

        step("Close the iOS keyboard");

        closeKeyboard();

        // Wait for keyboard to close
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // =========================================
        // STEP 5 - Scroll to Private Bike Insurance
        // =========================================

        step("Scroll to Private Bike Insurance");

        Assert.assertTrue(
                scrollDownUntilVisible(
                        IOSLocators.PRIVATE_BIKE_INSURANCE
                ),
                "Private Bike Insurance was not visible."
        );

        // =========================================
        // STEP 6 - Click Private Bike Insurance
        // =========================================

        step("Click Private Bike Insurance");

        WebElement bikeInsurance = wait.until(
                ExpectedConditions.elementToBeClickable(
                        IOSLocators.PRIVATE_BIKE_INSURANCE
                )
        );

        bikeInsurance.click();

        // Wait for Bike Insurance screen
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("======================================");
        System.out.println("PRIVATE BIKE INSURANCE SCREEN OPENED");
        System.out.println("======================================");

        // =========================================
        // STEP 7 - Verify Bike Insurance screen
        // =========================================

        step("Verify Bike Insurance screen is displayed");

        Assert.assertTrue(
                driver.getPageSource().contains(
                        "Bike Insurance"
                ),
                "Bike Insurance screen was not opened."
        );

        // =========================================
        // STEP 8 - Click top-right Cancel/Back control
        // =========================================

        step("Click Cancel / Back control");

        clickBikeInsuranceCancel();

        // =========================================
        // STEP 9 - Verify Login page
        // =========================================

        step("Verify user is redirected back to Login page");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        IOSLocators.LOGIN
                )
        );

        Assert.assertTrue(
                driver.findElement(
                        IOSLocators.LOGIN
                ).isDisplayed(),
                "Login page was not displayed after clicking Cancel."
        );

        System.out.println("======================================");
        System.out.println("LOGIN PAGE DISPLAYED");
        System.out.println("======================================");

        // =========================================
        // STEP 10 - Verify DOB field
        // =========================================

        step("Verify Date of Birth field is displayed");

        WebElement dobField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        IOSLocators.DOB
                )
        );

        Assert.assertTrue(
                dobField.isDisplayed(),
                "Date of Birth field is not displayed after returning to Login page."
        );

        // =========================================
        // STEP 11 - Verify age validation message
        // =========================================

        step("Verify age validation message remains displayed");

        boolean ageMessageStillDisplayed =
                driver.getPageSource().contains(
                        AGE_VALIDATION_MESSAGE
                );

        Assert.assertTrue(
                ageMessageStillDisplayed,
                "Age validation message disappeared after returning to Login page."
        );

        // =========================================
        // FINAL RESULT
        // =========================================

        System.out.println();
        System.out.println("======================================");
        System.out.println("TC44 PASSED");
        System.out.println("======================================");
        System.out.println("Login page opened");
        System.out.println("Today's date selected");
        System.out.println("Age validation message displayed");
        System.out.println("Keyboard closed");
        System.out.println("Private Bike Insurance opened");
        System.out.println("Top-right Cancel/Back control clicked");
        System.out.println("Returned to Login page");
        System.out.println("DOB field displayed");
        System.out.println("Age validation message remained");
        System.out.println("======================================");
    }

    // =========================================
    // CLOSE IOS KEYBOARD
    // =========================================

    private void closeKeyboard() {

        System.out.println("Closing iOS keyboard...");

        try {

            driver.executeScript(
                    "mobile: hideKeyboard"
            );

            System.out.println(
                    "iOS keyboard hide command executed."
            );

        } catch (Exception e) {

            System.out.println(
                    "Keyboard hide command failed: "
                            + e.getMessage()
            );
        }

        /*
         * Tap outside the DOB field.
         * This removes focus from the text field.
         */
        try {

            driver.executeScript(
                    "mobile: tap",
                    Map.of(
                            "x", 200,
                            "y", 120
                    )
            );

            System.out.println(
                    "Tapped outside DOB field."
            );

        } catch (Exception e) {

            System.out.println(
                    "Outside tap failed: "
                            + e.getMessage()
            );
        }
    }

    // =========================================
    // CLICK BIKE INSURANCE CANCEL / BACK
    // =========================================

    private void clickBikeInsuranceCancel() {

        System.out.println();
        System.out.println("======================================");
        System.out.println("FINDING BIKE INSURANCE CANCEL/BACK");
        System.out.println("======================================");

        /*
         * First try the actual element discovered
         * from the iOS page source.
         */

        try {

            WebDriverWait wait = new WebDriverWait(
                    driver,
                    Duration.ofSeconds(10)
            );

            WebElement topRightControl = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            BIKE_SCREEN_TOP_RIGHT_BUTTON
                    )
            );

            System.out.println(
                    "Top-right control found."
            );

            System.out.println(
                    "Element type: "
                            + topRightControl.getTagName()
            );

            System.out.println(
                    "Clicking top-right control..."
            );

            topRightControl.click();

            System.out.println(
                    "Top-right control clicked successfully."
            );

            return;

        } catch (Exception e) {

            System.out.println(
                    "Element click failed: "
                            + e.getMessage()
            );
        }

        /*
         * Fallback:
         * Direct coordinate tap based on the actual
         * element coordinates from the page source.
         *
         * Element:
         * x = 342
         * y = 55
         * width = 32
         * height = 34
         *
         * Center:
         * x = 358
         * y = 72
         */

        try {

            System.out.println(
                    "Using coordinate tap fallback."
            );

            driver.executeScript(
                    "mobile: tap",
                    Map.of(
                            "x", 358,
                            "y", 72
                    )
            );

            System.out.println(
                    "Top-right control tapped successfully."
            );

        } catch (Exception e) {

            Assert.fail(
                    "Could not click Bike Insurance Cancel/Back control.",
                    e
            );
        }
    }

    // =========================================
    // SCROLL DOWN
    // =========================================

    private boolean scrollDownUntilVisible(By locator) {

        for (int attempt = 0; attempt < 8; attempt++) {

            if (isVisible(locator)) {
                return true;
            }

            try {

                driver.executeScript(
                        "mobile: scroll",
                        Map.of(
                                "direction",
                                "down"
                        )
                );

                Thread.sleep(500);

            } catch (Exception ignored) {
            }
        }

        return isVisible(locator);
    }

    // =========================================
    // CHECK VISIBILITY
    // =========================================

    private boolean isVisible(By locator) {

        try {

            return new WebDriverWait(
                    driver,
                    Duration.ofSeconds(5)
            ).until(
                    ExpectedConditions.visibilityOfElementLocated(
                            locator
                    )
            ).isDisplayed();

        } catch (Exception ignored) {

            return false;
        }
    }
}