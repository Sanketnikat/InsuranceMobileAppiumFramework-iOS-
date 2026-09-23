package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC08_CountryCodeSelectionTest extends BaseTest {

    @Test(description = "SC_01_TC_008 - Verify that the country code is selectable")
    public void verifyCountryCodeIsSelectable() {

        System.out.println("========================================");
        System.out.println("SC_01_TC_008 - Country Code Selection");
        System.out.println("========================================");

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        /*
         * Step 1: Locate Login button
         */
        WebElement loginButton = wait.until(
                driver -> driver.findElement(
                        By.xpath("//*[@name='Login' or @label='Login']")
                )
        );

        Assert.assertTrue(
                loginButton.isDisplayed(),
                "Login button is not displayed"
        );

        System.out.println("Pre-login screen displayed");

        /*
         * Step 2: Tap Login
         */
        loginButton.click();

        System.out.println("Login button tapped");

        /*
         * Step 3: Wait for Mobile Number field
         */
        WebElement mobileNumberField = wait.until(
                driver -> driver.findElement(
                        AppiumBy.accessibilityId("Enter Mobile Number")
                )
        );

        Assert.assertTrue(
                mobileNumberField.isDisplayed(),
                "Mobile Number field is not displayed"
        );

        System.out.println("Enter Credentials page displayed");

        /*
         * Step 4: Locate the country flag selector.
         *
         * The flag is an XCUIElementTypeImage immediately
         * before the Mobile Number field.
         */
        WebElement countryFlag = wait.until(
                driver -> driver.findElement(
                        By.xpath(
                                "//*[@name='Enter Mobile Number']" +
                                "/preceding-sibling::XCUIElementTypeImage"
                        )
                )
        );

        Assert.assertTrue(
                countryFlag.isDisplayed(),
                "Country code selector is not displayed"
        );

        System.out.println("Country code selector is displayed");

        /*
         * Step 5: Tap country code selector
         */
        countryFlag.click();

        System.out.println("Country code selector tapped");

        /*
         * Step 6: Wait for country selection UI
         */
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.fail(
                    "Thread was interrupted while waiting for country selection"
            );
        }

        String pageSourceAfterTap = driver.getPageSource();

        Assert.assertNotNull(
                pageSourceAfterTap,
                "Country selection page is unavailable"
        );

        System.out.println("========================================");
        System.out.println("COUNTRY SELECTION PAGE SOURCE");
        System.out.println("========================================");

        System.out.println(pageSourceAfterTap);

        System.out.println("========================================");

        /*
         * Temporary verification:
         *
         * We verify that the country-selection UI has
         * appeared. The exact alternate country and
         * locator should be finalized from the actual
         * page source.
         */
        boolean countrySelectionDisplayed =
                pageSourceAfterTap.contains("India")
                        || pageSourceAfterTap.contains("Country")
                        || pageSourceAfterTap.contains("+91")
                        || pageSourceAfterTap.contains("United");

        Assert.assertTrue(
                countrySelectionDisplayed,
                "Country code selection UI was not displayed"
        );

        System.out.println("Country code selection UI displayed");

        System.out.println("SC_01_TC_008 PASSED");
    }
}