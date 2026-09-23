package tests.sprint1;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HidesKeyboard;
import locators.IOSLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

public class LoginPage {

    private final AppiumDriver driver;
    private final WebDriverWait wait;

    // =========================================
    // LOGIN SCREEN LOCATORS
    // =========================================

    private final By loginTitle =
            AppiumBy.accessibilityId("Login");

    /*
     * Confirmed from BrowserStack page source:
     *
     * XCUIElementTypeTextField
     * name="Enter Mobile Number"
     * label="Enter Mobile Number"
     */
    private final By mobileNumberField =
            AppiumBy.accessibilityId("Enter Mobile Number");

    private final By dateOfBirthField =
            AppiumBy.xpath(
                    "//XCUIElementTypeTextField" +
                    "[@name='Date of Birth']"
            );

    private final By loginViaOtpButton =
            AppiumBy.accessibilityId("Login via OTP");

    // =========================================
    // OTP SCREEN LOCATORS
    // =========================================

    private final By otpVerificationTitle =
            AppiumBy.accessibilityId("OTP Verification");

    private final By otpField =
            AppiumBy.xpath(
                    "//XCUIElementTypeStaticText" +
                    "[@name='OTP Verification']" +
                    "/following::XCUIElementTypeTextField[1]"
            );

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

        System.out.println(
                "Login button clicked."
        );
    }

    // =========================================
    // GET MOBILE NUMBER FIELD
    // =========================================

    private WebElement getMobileNumberField() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        mobileNumberField
                )
        );
    }

    // =========================================
    // ENTER MOBILE NUMBER
    // =========================================

    public void enterMobileNumber(String mobileNumber) {

        if (mobileNumber == null) {

            throw new IllegalArgumentException(
                    "Mobile number cannot be null."
            );
        }

        WebElement field =
                getMobileNumberField();

        field.click();

        /*
         * Clear current field.
         */
        try {

            field.clear();

        } catch (Exception e) {

            field.sendKeys(
                    Keys.chord(
                            Keys.COMMAND,
                            "a"
                    )
            );

            field.sendKeys(
                    Keys.BACK_SPACE
            );
        }

        /*
         * Send value directly to the actual
         * iOS TextField.
         */
        field.sendKeys(
                mobileNumber
        );

        System.out.println(
                "Attempted Mobile Number input: ["
                        + mobileNumber
                        + "]"
        );
    }

    // =========================================
    // GET MOBILE NUMBER VALUE
    // =========================================

    public String getMobileNumberValue() {

        WebElement field =
                getMobileNumberField();

        String value = "";

        try {

            value = field.getAttribute("value");

        } catch (Exception ignored) {
        }

        /*
         * Do NOT treat the placeholder
         * "Enter Mobile Number" as the actual
         * entered value.
         */
        if (value == null ||
                value.trim().isEmpty() ||
                value.equalsIgnoreCase("Enter Mobile Number")) {

            value = "";

            try {

                String text = field.getText();

                if (text != null &&
                        !text.equalsIgnoreCase("Enter Mobile Number")) {

                    value = text;
                }

            } catch (Exception ignored) {
            }
        }

        if (value == null) {
            value = "";
        }

        System.out.println(
                "Actual Mobile Number field value: ["
                        + value
                        + "]"
        );

        return value;
    }

    // =========================================
    // CLEAR MOBILE NUMBER
    // =========================================

    public void clearMobileNumber() {

        WebElement field =
                getMobileNumberField();

        field.click();

        try {

            field.clear();

        } catch (Exception e) {

            field.sendKeys(
                    Keys.chord(
                            Keys.COMMAND,
                            "a"
                    )
            );

            field.sendKeys(
                    Keys.BACK_SPACE
            );
        }

        System.out.println(
                "Mobile Number field cleared."
        );
    }

    // =========================================
    // VERIFY LOGIN VIA OTP BUTTON ENABLED
    // =========================================

    public boolean isLoginViaOtpButtonEnabled() {

        try {

            WebElement button =
                    wait.until(
                            ExpectedConditions.presenceOfElementLocated(
                                    loginViaOtpButton
                            )
                    );

            boolean enabled =
                    button.isEnabled();

            System.out.println(
                    "Login via OTP button enabled: "
                            + enabled
            );

            return enabled;

        } catch (Exception e) {

            System.out.println(
                    "Unable to find Login via OTP button: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // =========================================
    // VERIFY MOBILE NUMBER VALIDATION ERROR
    // =========================================

    public boolean isMobileNumberValidationErrorDisplayed() {

        /*
         * We intentionally inspect the current page
         * source rather than inventing a specific
         * error locator before we have confirmed it
         * from the application.
         */

        try {

            String pageSource =
                    driver.getPageSource();

            String source =
                    pageSource.toLowerCase();

            /*
             * Common validation text patterns.
             * If the application uses one of these,
             * this method will detect it.
             */
            String[] validationTexts = {

                    "invalid mobile",
                    "invalid mobile number",
                    "valid mobile number",
                    "mobile number is invalid",
                    "mobile number must",
                    "mobile number should",
                    "enter valid mobile",
                    "enter a valid mobile",
                    "10 digit",
                    "10-digit",
                    "minimum 10",
                    "at least 10",
                    "mobile number is required"
            };

            for (String validationText :
                    validationTexts) {

                if (source.contains(
                        validationText.toLowerCase()
                )) {

                    System.out.println(
                            "Mobile Number validation error detected: "
                                    + validationText
                    );

                    return true;
                }
            }

            /*
             * Also check XCUIElementTypeStaticText
             * elements that contain error-related
             * accessibility information.
             */
            String[] errorIndicators = {

                    "error",
                    "invalid",
                    "required",
                    "10 digit",
                    "10-digit"
            };

            for (String indicator :
                    errorIndicators) {

                if (source.contains(
                        indicator.toLowerCase()
                )) {

                    System.out.println(
                            "Potential validation indicator found: "
                                    + indicator
                    );

                    return true;
                }
            }

            System.out.println(
                    "No known Mobile Number validation error "
                            + "was detected in the current page source."
            );

            return false;

        } catch (Exception e) {

            System.out.println(
                    "Unable to inspect Mobile Number validation error: "
                            + e.getMessage()
            );

            return false;
        }
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

        field.sendKeys(
                Keys.chord(
                        Keys.COMMAND,
                        "a"
                )
        );

        field.sendKeys(
                Keys.BACK_SPACE
        );

        /*
         * Enter DOB one character at a time.
         */
        for (char character :
                dateOfBirth.toCharArray()) {

            field.sendKeys(
                    String.valueOf(character)
            );

            try {

                Thread.sleep(100);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        }

        dismissKeyboard();

        /*
         * Remove focus.
         */
        driver.executeScript(
                "mobile: tap",
                Map.of(
                        "x", 200,
                        "y", 120
                )
        );
    }

    // =========================================
    // DISMISS KEYBOARD
    // =========================================

    public void dismissKeyboard() {

        try {

            ((HidesKeyboard) driver)
                    .hideKeyboard();

        } catch (Exception ignored) {
        }

        try {

            driver.switchTo()
                    .activeElement()
                    .sendKeys(
                            Keys.ESCAPE
                    );

        } catch (Exception ignored) {
        }
    }

    // =========================================
    // CLICK LOGIN VIA OTP
    // =========================================

    public void clickLoginViaOtp() {

        WebElement button =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                loginViaOtpButton
                        )
                );

        if (!button.isEnabled()) {

            throw new RuntimeException(
                    "Login via OTP button is disabled."
            );
        }

        button.click();

        System.out.println(
                "Login via OTP button clicked."
        );
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

        System.out.println(
                "OTP entered."
        );
    }

    // =========================================
    // CLICK VALIDATE
    // =========================================

    public void clickValidate() {

        WebElement button =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                validateButton
                        )
                );

        if (!button.isEnabled()) {

            throw new RuntimeException(
                    "Validate button is disabled."
            );
        }

        button.click();

        System.out.println(
                "Validate button clicked."
        );
    }

    // =========================================
    // LOADING INDICATOR
    // =========================================

    public boolean isLoadingIndicatorDisplayed() {

        try {

            WebElement indicator =
                    new WebDriverWait(
                            driver,
                            Duration.ofSeconds(5)
                    ).until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    IOSLocators.LOADING_INDICATOR
                            )
                    );

            return indicator.isDisplayed();

        } catch (Exception ignored) {

            return false;
        }
    }

    // =========================================
    // GET PAGE SOURCE
    // =========================================

    public String getPageSource() {

        return driver.getPageSource();
    }
}