package tests.sprint1;

import framework.BaseTest;
import locators.IOSLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;

public class TC44_SimultaneousCarAndBikeInsuranceTest extends BaseTest {

    // =========================================
    // DESTINATION LOCATORS
    // =========================================

    private static final By CAR_INSURANCE_SCREEN =
            org.openqa.selenium.By.name("Car Insurance");

    private static final By BIKE_INSURANCE_SCREEN =
            org.openqa.selenium.By.name("Bike Insurance");

    @Test(
            description = "Verify only one insurance page opens when Car and Bike Insurance are tapped simultaneously",
            groups = {"sprint1", "sprint2", "pre-login"}
    )
    public void TC04_simultaneousCarAndBikeInsuranceClicks() {

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
        // STEP 2 - Scroll to insurance products
        // =========================================

        step("Scroll to insurance products");

        boolean productsVisible =
                scrollDownUntilVisible(
                        IOSLocators.PRIVATE_CAR_INSURANCE
                );

        Assert.assertTrue(
                productsVisible,
                "Private Car Insurance was not found."
        );

        // =========================================
        // STEP 3 - Find Car Insurance
        // =========================================

        step("Find Private Car Insurance button");

        WebElement carButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        IOSLocators.PRIVATE_CAR_INSURANCE
                )
        );

        // =========================================
        // STEP 4 - Find Bike Insurance
        // =========================================

        step("Find Private Bike Insurance button");

        WebElement bikeButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        IOSLocators.PRIVATE_BIKE_INSURANCE
                )
        );

        Assert.assertTrue(
                carButton.isDisplayed(),
                "Private Car Insurance is not displayed."
        );

        Assert.assertTrue(
                bikeButton.isDisplayed(),
                "Private Bike Insurance is not displayed."
        );

        // =========================================
        // STEP 5 - Calculate coordinates
        // =========================================

        step("Calculate Car and Bike touch coordinates");

        int carX =
                carButton.getLocation().getX()
                        + carButton.getSize().getWidth() / 2;

        int carY =
                carButton.getLocation().getY()
                        + carButton.getSize().getHeight() / 2;

        int bikeX =
                bikeButton.getLocation().getX()
                        + bikeButton.getSize().getWidth() / 2;

        int bikeY =
                bikeButton.getLocation().getY()
                        + bikeButton.getSize().getHeight() / 2;

        System.out.println();
        System.out.println("======================================");
        System.out.println("MULTI-TOUCH COORDINATES");
        System.out.println("======================================");
        System.out.println(
                "CAR  -> X: " + carX + " Y: " + carY
        );
        System.out.println(
                "BIKE -> X: " + bikeX + " Y: " + bikeY
        );
        System.out.println("======================================");

        // =========================================
        // STEP 6 - Create two touch pointers
        // =========================================

        step("Create two simultaneous touch pointers");

        PointerInput finger1 =
                new PointerInput(
                        PointerInput.Kind.TOUCH,
                        "finger1"
                );

        PointerInput finger2 =
                new PointerInput(
                        PointerInput.Kind.TOUCH,
                        "finger2"
                );

        // =========================================
        // STEP 7 - Create multi-touch sequences
        // =========================================

        Sequence carTouch =
                new Sequence(finger1, 0);

        Sequence bikeTouch =
                new Sequence(finger2, 0);

        /*
         * Both fingers move to their respective buttons.
         */

        carTouch.addAction(
                finger1.createPointerMove(
                        Duration.ZERO,
                        PointerInput.Origin.viewport(),
                        carX,
                        carY
                )
        );

        bikeTouch.addAction(
                finger2.createPointerMove(
                        Duration.ZERO,
                        PointerInput.Origin.viewport(),
                        bikeX,
                        bikeY
                )
        );

        /*
         * Both fingers touch DOWN.
         *
         * These actions occupy the same W3C action tick.
         */

        carTouch.addAction(
                finger1.createPointerDown(
                        PointerInput.MouseButton.LEFT.asArg()
                )
        );

        bikeTouch.addAction(
                finger2.createPointerDown(
                        PointerInput.MouseButton.LEFT.asArg()
                )
        );

        /*
         * Hold both touches briefly.
         */

        carTouch.addAction(
                new Pause(
                        finger1,
                        Duration.ofMillis(100)
                )
        );

        bikeTouch.addAction(
                new Pause(
                        finger2,
                        Duration.ofMillis(100)
                )
        );

        /*
         * Both fingers release.
         */

        carTouch.addAction(
                finger1.createPointerUp(
                        PointerInput.MouseButton.LEFT.asArg()
                )
        );

        bikeTouch.addAction(
                finger2.createPointerUp(
                        PointerInput.MouseButton.LEFT.asArg()
                )
        );

        // =========================================
        // STEP 8 - Perform multi-touch gesture
        // =========================================

        step("Perform simultaneous Car and Bike touch");

        System.out.println();
        System.out.println("======================================");
        System.out.println("STARTING MULTI-TOUCH");
        System.out.println("======================================");
        System.out.println("Finger 1 -> Car Insurance");
        System.out.println("Finger 2 -> Bike Insurance");
        System.out.println("Both touches are sent in the same action.");
        System.out.println("======================================");

        driver.perform(
                Arrays.asList(
                        carTouch,
                        bikeTouch
                )
        );

        // =========================================
        // STEP 9 - Wait for application response
        // =========================================

        step("Wait for application navigation");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // =========================================
        // STEP 10 - Check final navigation
        // =========================================

        step("Check which insurance page opened");

        boolean carScreenOpened =
                isVisible(CAR_INSURANCE_SCREEN);

        boolean bikeScreenOpened =
                isVisible(BIKE_INSURANCE_SCREEN);

        System.out.println();
        System.out.println("======================================");
        System.out.println("FINAL NAVIGATION RESULT");
        System.out.println("======================================");
        System.out.println(
                "Car Insurance screen : "
                        + carScreenOpened
        );
        System.out.println(
                "Bike Insurance screen: "
                        + bikeScreenOpened
        );
        System.out.println("======================================");

        // =========================================
        // STEP 11 - Validate overlapping navigation
        // =========================================

        /*
         * Both pages being opened is the defect.
         */

        Assert.assertFalse(
                carScreenOpened && bikeScreenOpened,
                "FAIL: Car Insurance and Bike Insurance pages opened together. Navigation requests overlapped."
        );

        /*
         * At least one page should open.
         */

        Assert.assertTrue(
                carScreenOpened || bikeScreenOpened,
                "FAIL: Neither Car Insurance nor Bike Insurance page opened."
        );

        // =========================================
        // FINAL RESULT
        // =========================================

        System.out.println();
        System.out.println("======================================");
        System.out.println("TC44 RESULT");
        System.out.println("======================================");

        if (carScreenOpened) {

            System.out.println(
                    "Car Insurance page opened."
            );

        } else {

            System.out.println(
                    "Bike Insurance page opened."
            );
        }

        System.out.println(
                "Only ONE insurance page was opened."
        );

        System.out.println(
                "Concurrent navigation was handled correctly."
        );

        System.out.println("======================================");
    }

    // =========================================
    // SCROLL
    // =========================================

    private boolean scrollDownUntilVisible(By locator) {

        for (int attempt = 0; attempt < 8; attempt++) {

            if (isVisible(locator)) {
                return true;
            }

            try {

                driver.executeScript(
                        "mobile: scroll",
                        java.util.Map.of(
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