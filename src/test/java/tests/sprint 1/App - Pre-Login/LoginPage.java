package tests.sprint1;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HidesKeyboard;
import locators.IOSLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final AppiumDriver driver;
    private final WebDriverWait wait;

    // =========================================
    // LOGIN SCREEN LOCATORS
    // =========================================

    private final By loginTitle =
            AppiumBy.accessibilityId("Login");

    private final By mobileNumberField =
            AppiumBy.xpath(
                    "//XCUIElementTypeTextField[" +
                    "not(@name='Date of Birth')" +
                    "]"
            );

    private final By dateOfBirthField =
            AppiumBy.xpath(
                    "//XCUIElementTypeStaticText" +
                    "[@name='Date of Birth / Date of Incorporation*']" +
                    "/following::XCUIElementTypeTextField[1]"
            );

    private final By loginViaOtpButton =
            AppiumBy.accessibilityId("Login via OTP");

    // =========================================
    // OTP SCREEN LOCATORS
    // =========================================

    private final By otpVerificationTitle =
            AppiumBy.accessibilityId("OTP Verification");

    /*
     * OTP field from the actual OTP screen XML:
     *
     * XCUIElementTypeTextField
     * x="24"
     * y="598"
     * width="342"
     * height="42"
     *
     * It does not have a name or label.
     */
    private final By otpField =
            AppiumBy.xpath(
                    "//XCUIElementTypeStaticText" +
                    "[@name='OTP Verification']" +
                    "/following::XCUIElementTypeTextField[1]"
            );

    /*
     * Validate button:
     *
     * XCUIElementTypeButton
     * name="Validate"
     * label="Validate"
     */
    private final By validateButton =
            AppiumBy.xpath(
                    "//XCUIElementTypeButton" +
                    "[@name='Validate' or @label='Validate']"
            );

    // =========================================
    // CONSTRUCTOR
    // =========================================

    public LoginPage(AppiumDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );
    }

    // =========================================
    // CLICK LOGIN
    // =========================================

    public void clickLogin() {

        WebElement login =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                loginTitle
                        )
                );

        login.click();
    }

    // =========================================
    // ENTER MOBILE NUMBER
    // =========================================

    public void enterMobileNumber(String mobileNumber) {

        if (mobileNumber == null ||
                mobileNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Mobile number cannot be null or empty."
            );
        }

        WebElement field =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                mobileNumberField
                        )
                );

        field.click();
        driver.switchTo().activeElement().sendKeys(
                Keys.chord(Keys.COMMAND, "a"),
                Keys.BACK_SPACE
        );
        new Actions(driver)
                .sendKeys(mobileNumber)
                .perform();
    }

    // =========================================
    // ENTER DATE OF BIRTH
    // =========================================

    public void enterDateOfBirth(String dateOfBirth) {

        if (dateOfBirth == null ||
                dateOfBirth.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Date of birth cannot be null or empty."
            );
        }

        WebElement field =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                dateOfBirthField
                        )
                );

        field.click();

        // Select all existing text
        field.sendKeys(
                "\uE009" + "a"
        );

        // Delete selected text
        field.sendKeys(
                "\uE003"
        );

        // Allow iOS field to update
        try {

            Thread.sleep(500);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Interrupted while clearing DOB field.",
                    e
            );
        }

                // Enter masked DOB fields one character at a time so iOS does not drop input.
                for (char character : dateOfBirth.toCharArray()) {
                        new Actions(driver)
                                        .sendKeys(String.valueOf(character))
                                        .perform();

                        try {
                                Thread.sleep(100);
                        } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                throw new RuntimeException(
                                                "Interrupted while entering date of birth.",
                                                e
                                );
                        }
                }
    }

    /**
     * Closes the iOS keyboard so controls below the form remain available.
     */
    public void dismissKeyboard() {

        try {

            ((HidesKeyboard) driver).hideKeyboard();

        } catch (Exception ignored) {
        }

        // iOS can report a successful hide request while retaining focus.
        // Escape closes the active native keyboard when that happens.
        try {

            driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);

        } catch (Exception ignored) {
        }
    }

    // =========================================
    // CLICK LOGIN VIA OTP
    // =========================================

    public void clickLoginViaOtp() {

        WebElement button = null;

        try {

            // Primary locator
            button =
                    wait.until(
                            ExpectedConditions.elementToBeClickable(
                                    loginViaOtpButton
                            )
                    );

        } catch (Exception e) {

            // Fallback XPath
            By fallbackLocator =
                    AppiumBy.xpath(
                            "//XCUIElementTypeButton" +
                            "[@name='Login via OTP' " +
                            "or @label='Login via OTP']"
                    );

            button =
                    wait.until(
                            ExpectedConditions.elementToBeClickable(
                                    fallbackLocator
                            )
                    );
        }

        if (!button.isEnabled()) {

            throw new RuntimeException(
                    "Login via OTP button is disabled. " +
                    "Mobile number or DOB may be invalid."
            );
        }

        button.click();
    }

    // =========================================
    // WAIT FOR OTP SCREEN
    // =========================================

    public void waitForOtpScreen() {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        otpVerificationTitle
                )
        );

        // Also make sure OTP field is available
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        otpField
                )
        );
    }

    // =========================================
    // ENTER OTP
    // =========================================

    public void enterOtp(String otp) {

        if (otp == null ||
                otp.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "OTP cannot be null or empty."
            );
        }

        WebElement field =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                otpField
                        )
                );

        field.click();

        field.clear();

        field.sendKeys(otp);

        // Verify that OTP field contains something
        String enteredValue = field.getAttribute("value");

        System.out.println(
                "======================================"
        );

        System.out.println(
                "OTP FIELD VALUE: " + enteredValue
        );

        System.out.println(
                "======================================"
        );
    }

    // =========================================
    // CLICK VALIDATE
    // =========================================

    public void clickValidate() {

        WebElement button = null;

        try {

            // Primary XPath locator
            button =
                    wait.until(
                            ExpectedConditions.elementToBeClickable(
                                    validateButton
                            )
                    );

        } catch (Exception e) {

            System.out.println(
                    "Primary Validate locator failed."
            );

            System.out.println(
                    "Trying accessibility ID..."
            );

            try {

                button =
                        wait.until(
                                ExpectedConditions.elementToBeClickable(
                                        AppiumBy.accessibilityId(
                                                "Validate"
                                        )
                                )
                        );

            } catch (Exception secondException) {

                throw new RuntimeException(
                        "Validate button could not be located.",
                        secondException
                );
            }
        }

        if (!button.isEnabled()) {

            throw new RuntimeException(
                    "Validate button is disabled."
            );
        }

        System.out.println(
                "======================================"
        );

        System.out.println(
                "VALIDATE BUTTON FOUND"
        );

        System.out.println(
                "======================================"
        );

        button.click();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "VALIDATE BUTTON CLICKED"
        );

        System.out.println(
                "======================================"
        );
    }

    // =========================================
    // GET PAGE SOURCE
    // =========================================

    public boolean isLoadingIndicatorDisplayed() {

        try {
            WebElement indicator = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            IOSLocators.LOADING_INDICATOR
                    )
            );
            return indicator.isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }

    public String getPageSource() {

        return driver.getPageSource();
    }
}
