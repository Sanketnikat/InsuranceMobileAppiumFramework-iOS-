package tests.sprint1;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SC_01_TC_045_ValidateMobileNumberRejectsAlphabetsAndSpecialCharactersTest
        extends BaseTest {

    @Test(
            description = "SC_01_TC_045 - Verify alphabets and special characters are rejected in Mobile Number field",
            groups = {"sprint1", "login"}
    )
    public void SC_01_TC_045_ValidateMobileNumberRejectsAlphabetsAndSpecialCharacters() {

        LoginPage loginPage =
                new LoginPage(driver);

        // =========================================
        // STEP 1 - PRE-LOGIN
        // =========================================

        step(
                "Launch application and land on pre-login screen"
        );

        System.out.println(
                "Pre-login screen displayed."
        );

        // =========================================
        // STEP 2 - TAP LOGIN
        // =========================================

        step(
                "Tap Login"
        );

        loginPage.clickLogin();

        // =========================================
        // STEP 3 - ALPHABETS
        // =========================================

        step(
                "Enter alphabets in Mobile Number field"
        );

        loginPage.enterMobileNumber(
                "ABCabc"
        );

        String alphabetValue =
                loginPage.getMobileNumberValue();

        System.out.println(
                "Value after alphabet input: ["
                        + alphabetValue
                        + "]"
        );

        Assert.assertFalse(
                alphabetValue.matches(
                        ".*[A-Za-z].*"
                ),
                "Alphabetic characters should be rejected."
        );

        System.out.println(
                "PASS: Alphabetic characters rejected."
        );

        // =========================================
        // STEP 4 - CLEAR
        // =========================================

        loginPage.clearMobileNumber();

        // =========================================
        // STEP 5 - SPECIAL CHARACTERS
        // =========================================

        step(
                "Enter special characters in Mobile Number field"
        );

        loginPage.enterMobileNumber(
                "@#$%^&*()"
        );

        String specialCharacterValue =
                loginPage.getMobileNumberValue();

        System.out.println(
                "Value after special character input: ["
                        + specialCharacterValue
                        + "]"
        );

        Assert.assertFalse(
                specialCharacterValue.matches(
                        ".*[@#$%^&*()].*"
                ),
                "Special characters should be rejected."
        );

        System.out.println(
                "PASS: Special characters rejected."
        );

        // =========================================
        // STEP 6 - CLEAR
        // =========================================

        loginPage.clearMobileNumber();

        // =========================================
        // STEP 7 - NUMERIC INPUT
        // =========================================

        step(
                "Enter numeric input in Mobile Number field"
        );

        String expectedMobileNumber =
                "7263935191";

        loginPage.enterMobileNumber(
                expectedMobileNumber
        );

        String numericValue =
                loginPage.getMobileNumberValue();

        System.out.println(
                "Expected numeric value: ["
                        + expectedMobileNumber
                        + "]"
        );

        System.out.println(
                "Actual numeric value: ["
                        + numericValue
                        + "]"
        );

        /*
         * Numeric input must be accepted.
         */
        Assert.assertTrue(
                numericValue.matches("\\d+"),
                "Mobile Number field should accept numeric input. " +
                "Actual value: [" + numericValue + "]"
        );

        System.out.println(
                "PASS: Numeric input accepted."
        );

        // =========================================
        // FINAL RESULT
        // =========================================

        System.out.println(
                "======================================"
        );

        System.out.println(
                "SC_01_TC_045 PASSED"
        );

        System.out.println(
                "Alphabetic characters rejected."
        );

        System.out.println(
                "Special characters rejected."
        );

        System.out.println(
                "Numeric input accepted."
        );

        System.out.println(
                "======================================"
        );
    }
}