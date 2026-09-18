package locators;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class IOSLocators {

    // =========================================
    // WELCOME / SPLASH SCREEN
    // =========================================

    public static final By LOGIN =
            AppiumBy.accessibilityId("Login");

    public static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");

    // =========================================
    // LOGIN SCREEN
    // =========================================

    public static final By OTP_HINT =
            AppiumBy.accessibilityId(
                    "Your details will be verified by a secure OTP"
            );

    public static final By MOBILE_NUMBER =
            AppiumBy.accessibilityId("Mobile Number*");

    public static final By MOBILE_NUMBER_INPUT =
            AppiumBy.xpath(
                    "//XCUIElementTypeTextField[" +
                    "not(@name='Date of Birth')" +
                    "]"
            );

    public static final By DOB =
            AppiumBy.accessibilityId(
                    "Date of Birth / Date of Incorporation*"
            );

    public static final By DOB_INPUT =
            AppiumBy.xpath(
                    "//XCUIElementTypeStaticText" +
                    "[@name='Date of Birth / Date of Incorporation*']" +
                    "/following::XCUIElementTypeTextField[1]"
            );

    public static final By LOGIN_VIA_OTP =
            AppiumBy.accessibilityId("Login via OTP");

    // =========================================
    // LOADING INDICATOR
    // =========================================

    public static final By LOADING_INDICATOR =
            AppiumBy.iOSClassChain(
                    "**/XCUIElementTypeActivityIndicator"
            );

    // =========================================
    // PRODUCTS
    // =========================================

    public static final By EXPLORE_PRODUCTS =
            AppiumBy.accessibilityId("Explore Our Products");

    public static final By PRIVATE_CAR_INSURANCE =
            AppiumBy.accessibilityId("Private Car Insurance");

    public static final By PRIVATE_BIKE_INSURANCE =
            AppiumBy.accessibilityId("Private Bike Insurance");

    public static final By COMMERCIAL_VEHICLE_INSURANCE =
            AppiumBy.accessibilityId("Commercial Vehicle Insurance");

    public static final By HEALTH_INSURANCE =
            AppiumBy.accessibilityId("Health Insurance");

    public static final By TERM_LIFE_INSURANCE =
            AppiumBy.accessibilityId("Term Life Insurance");

    // =========================================
    // OTP SCREEN
    // =========================================

    public static final By OTP_VERIFICATION =
            AppiumBy.accessibilityId("OTP Verification");

    public static final By OTP_INPUT =
            AppiumBy.xpath(
                    "//XCUIElementTypeStaticText" +
                    "[@name='OTP Verification']" +
                    "/following::XCUIElementTypeTextField[1]"
            );

    public static final By RESEND_OTP =
            AppiumBy.accessibilityId("Resend OTP");

    public static final By VALIDATE =
            AppiumBy.xpath(
                    "//XCUIElementTypeButton" +
                    "[@name='Validate' or @label='Validate']"
            );
}