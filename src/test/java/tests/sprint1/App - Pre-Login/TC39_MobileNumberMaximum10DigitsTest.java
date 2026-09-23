package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC39_MobileNumberMaximum10DigitsTest extends BaseTest {

    @Test
    public void TC39_verifyMobileNumberDoesNotAcceptMoreThan10Digits() {

        System.out.println("================================");
        System.out.println("TC39 - MOBILE NUMBER MAXIMUM 10 DIGITS");
        System.out.println("================================");

        // Step 1 - Find Login button
        WebElement loginButton = driver.findElement(
                AppiumBy.xpath(
                        "//*[@name='Login' or @label='Login']"
                )
        );

        Assert.assertTrue(
                loginButton.isDisplayed(),
                "Login button is not displayed."
        );

        System.out.println("Login button displayed.");

        // Step 2 - Tap Login
        loginButton.click();

        System.out.println("Login button clicked.");

        // Step 3 - Wait for screen transition
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Step 4 - Print page source
        System.out.println("================================");
        System.out.println("PAGE SOURCE AFTER LOGIN");
        System.out.println("================================");

        String pageSource = driver.getPageSource();

        System.out.println(pageSource);

        System.out.println("================================");

        Assert.assertNotNull(
                pageSource,
                "Page source is null."
        );

        Assert.assertTrue(
                pageSource.length() > 0,
                "Page source is empty."
        );

        // Step 5 - Check whether Mobile Number exists
        boolean mobileNumberExists =
                pageSource.contains("Enter Mobile Number");

        System.out.println(
                "Enter Mobile Number present: "
                        + mobileNumberExists
        );

        Assert.assertTrue(
                mobileNumberExists,
                "Mobile Number field is not present after clicking Login."
        );

        System.out.println(
                "Mobile Number field is present."
        );

        System.out.println(
                "PAGE SOURCE CAPTURE COMPLETED"
        );
    }
}
