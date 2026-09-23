package tests.sprint1;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC42_VerifyLoadingIndicatorWhileLoginRequestIsProcessing
        extends BaseTest {

    @Test(
            description = "TC42 - Verify loading indicator while login request is processing",
            groups = {"sprint1", "login"}
    )
    public void TC42_VerifyLoadingIndicatorWhileLoginRequestIsProcessing() {

        LoginPage loginPage = new LoginPage(driver);

        // =========================================
        // STEP 1 - TAP LOGIN
        // =========================================

        step("Tap Login");

        loginPage.clickLogin();

        // =========================================
        // STEP 2 - ENTER MOBILE NUMBER
        // =========================================

        step("Enter valid mobile number");

        loginPage.enterMobileNumber(
                "7263935191"
        );

        // =========================================
        // STEP 3 - ENTER DATE OF BIRTH
        // =========================================

        step("Enter valid date of birth");

        loginPage.enterDateOfBirth(
                "04/05/2000"
        );

        // =========================================
        // STEP 4 - TAP LOGIN VIA OTP
        // =========================================

        step("Tap Login via OTP");

        loginPage.clickLoginViaOtp();

        // =========================================
        // STEP 5 - VERIFY LOADING INDICATOR
        // =========================================

        step("Verify loading indicator while login request is processing");

        /*
         * Give the application a short amount of time to start
         * the login request and display the loading indicator.
         *
         * The actual loading-indicator detection remains inside
         * LoginPage.isLoadingIndicatorDisplayed().
         */
        boolean loadingIndicatorDisplayed = false;

        long startTime = System.currentTimeMillis();
        long timeout = 5000;

        while (System.currentTimeMillis() - startTime < timeout) {

            try {

                if (loginPage.isLoadingIndicatorDisplayed()) {

                    loadingIndicatorDisplayed = true;

                    System.out.println(
                            "Loading indicator detected."
                    );

                    break;
                }

            } catch (Exception e) {

                /*
                 * The loader may not yet exist in the UI hierarchy.
                 * Continue polling until timeout.
                 */
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // =========================================
        // STEP 6 - ASSERT RESULT
        // =========================================

        Assert.assertTrue(
                loadingIndicatorDisplayed,
                "Loading indicator should appear while the login request is being processed."
        );

        // =========================================
        // TEST RESULT
        // =========================================

        System.out.println(
                "======================================"
        );

        System.out.println(
                "TC42 PASSED"
        );

        System.out.println(
                "Loading indicator is displayed " +
                "while login request is processing."
        );

        System.out.println(
                "======================================"
        );
    }
}