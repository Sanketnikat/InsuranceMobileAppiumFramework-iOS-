package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import locators.IOSLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SimultaneousLoginAndBikeInsuranceTest extends BaseTest {

    private static final By BIKE_INSURANCE_SCREEN =
            AppiumBy.accessibilityId("Bike Insurance");

    private static final By OTP_VERIFICATION =
            AppiumBy.accessibilityId("OTP Verification");

    @Test(
            description = "BD2M-675 - TC04 - Handle simultaneous Login via OTP and Private Bike Insurance clicks",
            groups = {"sprint1", "sprint2", "pre-login"}
    )
    public void TC04_simultaneousLoginAndBikeInsuranceClicks() throws Exception {

        LoginPage loginPage = new LoginPage(driver);

        // =========================================
        // STEP 1 - OPEN LOGIN PAGE
        // =========================================

        step("Open the Login page");

        loginPage.clickLogin();

        // =========================================
        // STEP 2 - ENTER MOBILE NUMBER
        // =========================================

        step("Enter valid mobile number");

        loginPage.enterMobileNumber("8921639271");

        // =========================================
        // STEP 3 - ENTER DATE OF BIRTH
        // =========================================

        step("Enter valid date of birth");

        loginPage.enterDateOfBirth("01/01/1999");

        // =========================================
        // STEP 4 - CLOSE KEYBOARD
        // =========================================

        step("Close keyboard after entering credentials");

        closeKeyboard();

        // =========================================
        // STEP 5 - TAP SAFE AREA
        // =========================================

        step("Tap safe area after closing keyboard");

        tapSafeArea();

        // =========================================
        // STEP 6 - VERIFY LOGIN VIA OTP
        // =========================================

        step("Verify Login via OTP is visible");

        Assert.assertTrue(
                isVisible(IOSLocators.LOGIN_VIA_OTP),
                "Login via OTP was not visible."
        );

        // =========================================
        // STEP 7 - SCROLL TO BIKE INSURANCE
        // =========================================

        step("Scroll down to locate Private Bike Insurance");

        Assert.assertTrue(
                scrollDownUntilVisible(
                        IOSLocators.PRIVATE_BIKE_INSURANCE
                ),
                "Private Bike Insurance was not visible after scrolling down."
        );

        // =========================================
        // STEP 8 - VERIFY BIKE INSURANCE
        // =========================================

        step("Verify Private Bike Insurance is visible");

        Assert.assertTrue(
                isVisible(IOSLocators.PRIVATE_BIKE_INSURANCE),
                "Private Bike Insurance was not visible."
        );

        // =========================================
        // IMPORTANT
        // =========================================
        //
        // After scrolling, Login via OTP may no longer
        // be inside the visible viewport.
        //
        // Therefore we verify that Appium can locate it
        // before starting the simultaneous click attempt.
        //
        // =========================================

        step("Verify Login via OTP can still be located");

        Assert.assertTrue(
                isPresent(IOSLocators.LOGIN_VIA_OTP),
                "Login via OTP could not be located after scrolling."
        );

        // =========================================
        // STEP 9 - PREPARE SIMULTANEOUS CLICK
        // =========================================

        step(
                "Prepare simultaneous Login via OTP and Private Bike Insurance clicks"
        );

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        CountDownLatch readyLatch =
                new CountDownLatch(2);

        CountDownLatch startLatch =
                new CountDownLatch(1);

        try {

            Callable<Void> loginClick = () -> {

                readyLatch.countDown();

                // Wait until both click tasks are ready.
                startLatch.await();

                System.out.println(
                        "THREAD 1 -> Clicking Login via OTP"
                );

                clickButton(
                        IOSLocators.LOGIN_VIA_OTP
                );

                System.out.println(
                        "THREAD 1 -> Login via OTP click sent"
                );

                return null;
            };

            Callable<Void> bikeClick = () -> {

                readyLatch.countDown();

                // Wait until both click tasks are ready.
                startLatch.await();

                System.out.println(
                        "THREAD 2 -> Clicking Private Bike Insurance"
                );

                clickButton(
                        IOSLocators.PRIVATE_BIKE_INSURANCE
                );

                System.out.println(
                        "THREAD 2 -> Private Bike Insurance click sent"
                );

                return null;
            };

            // =========================================
            // SUBMIT BOTH TASKS
            // =========================================

            Future<Void> loginFuture =
                    executor.submit(loginClick);

            Future<Void> bikeFuture =
                    executor.submit(bikeClick);

            // =========================================
            // WAIT UNTIL BOTH THREADS ARE READY
            // =========================================

            readyLatch.await();

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "BOTH CLICK THREADS ARE READY"
            );

            System.out.println(
                    "Releasing both clicks simultaneously..."
            );

            System.out.println(
                    "======================================"
            );

            // =========================================
            // RELEASE BOTH THREADS AT THE SAME TIME
            // =========================================

            startLatch.countDown();

            // =========================================
            // WAIT FOR BOTH CLICK TASKS
            // =========================================

            try {

                loginFuture.get();

            } catch (Exception e) {

                System.out.println(
                        "Login via OTP click result: "
                                + e.getMessage()
                );
            }

            try {

                bikeFuture.get();

            } catch (Exception e) {

                System.out.println(
                        "Private Bike Insurance click result: "
                                + e.getMessage()
                );
            }

        } finally {

            executor.shutdownNow();
        }

        // =========================================
        // STEP 10 - WAIT FOR APPLICATION TO SETTLE
        // =========================================

        step("Wait for application to settle");

        try {

            Thread.sleep(3000);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Interrupted while waiting for application to settle.",
                    e
            );
        }

        // =========================================
        // STEP 11 - CHECK FINAL SCREEN
        // =========================================

        step(
                "Verify application settled on one of the intended destinations"
        );

        boolean bikeInsuranceOpened =
                isVisible(BIKE_INSURANCE_SCREEN);

        boolean otpScreenOpened =
                isVisible(OTP_VERIFICATION);

        System.out.println(
                "======================================"
        );

        System.out.println(
                "FINAL APPLICATION STATE"
        );

        System.out.println(
                "Bike Insurance Screen : "
                        + bikeInsuranceOpened
        );

        System.out.println(
                "OTP Verification     : "
                        + otpScreenOpened
        );

        System.out.println(
                "======================================"
        );

        // =========================================
        // STEP 12 - FINAL ASSERTION
        // =========================================

        Assert.assertTrue(
                bikeInsuranceOpened || otpScreenOpened,
                "The application did not settle on either intended destination."
        );

        System.out.println(
                "======================================"
        );

        System.out.println(
                "TC04 COMPLETED"
        );

        System.out.println(
                "======================================"
        );
    }

    // =========================================
    // CLOSE KEYBOARD
    // =========================================

    private void closeKeyboard() {

        try {

            driver.executeScript(
                    "mobile: hideKeyboard"
            );

            System.out.println(
                    "Keyboard closed successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Keyboard was already closed or could not be hidden."
            );

            System.out.println(
                    "Reason: "
                            + e.getMessage()
            );
        }
    }

    // =========================================
    // TAP SAFE AREA
    // =========================================

    private void tapSafeArea() {

        try {

            driver.executeScript(
                    "mobile: tap",
                    Map.of(
                            "x", 200,
                            "y", 150
                    )
            );

            System.out.println(
                    "Safe area tapped successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "Safe-area tap failed: "
                            + e.getMessage()
            );
        }
    }

    // =========================================
    // CLICK BUTTON
    // =========================================

    private void clickButton(By locator) {

        new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        )
                .until(
                        ExpectedConditions.elementToBeClickable(
                                locator
                        )
                )
                .click();
    }

    // =========================================
    // CHECK ELEMENT IS PRESENT
    // =========================================

    private boolean isPresent(By locator) {

        try {

            return !driver.findElements(locator).isEmpty();

        } catch (Exception e) {

            return false;
        }
    }

    // =========================================
    // CHECK ELEMENT IS VISIBLE
    // =========================================

    private boolean isVisible(By locator) {

        try {

            return new WebDriverWait(
                    driver,
                    Duration.ofSeconds(5)
            )
                    .until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    locator
                            )
                    )
                    .isDisplayed();

        } catch (Exception e) {

            return false;
        }
    }

    // =========================================
    // SCROLL DOWN UNTIL ELEMENT IS VISIBLE
    // =========================================

    private boolean scrollDownUntilVisible(By locator) {

        for (int attempt = 0; attempt < 8; attempt++) {

            System.out.println(
                    "Scroll attempt: "
                            + (attempt + 1)
            );

            if (isVisible(locator)) {

                System.out.println(
                        "Element found."
                );

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

            } catch (Exception e) {

                System.out.println(
                        "Scroll failed: "
                                + e.getMessage()
                );
            }
        }

        return isVisible(locator);
    }
}