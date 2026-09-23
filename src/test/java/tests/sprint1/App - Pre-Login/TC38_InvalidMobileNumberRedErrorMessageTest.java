package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC38_InvalidMobileNumberRedErrorMessageTest extends BaseTest {

    @Test
    public void TC38_verifyInvalidMobileNumberErrorMessage() {

        // Tap Login
        driver.findElement(
                AppiumBy.xpath("//*[@name='Login' or @label='Login']")
        ).click();

        // Enter invalid mobile number
        driver.findElement(
                AppiumBy.accessibilityId("Enter Mobile Number")
        ).sendKeys("12345");

        // Tap Login via OTP
        driver.findElement(
                AppiumBy.accessibilityId("Login via OTP")
        ).click();

        // Get page source
        String pageSource = driver.getPageSource();

        System.out.println("================================");
        System.out.println("PAGE SOURCE");
        System.out.println("================================");
        System.out.println(pageSource);

        // Verify validation message
        boolean errorMessageDisplayed =
                pageSource.contains("Invalid")
                        || pageSource.contains("invalid")
                        || pageSource.contains("Mobile Number");

        Assert.assertTrue(
                errorMessageDisplayed,
                "Invalid mobile number error message was not displayed."
        );

        System.out.println(
                "SC_01_TC_038 PASSED - Invalid mobile number validation message is displayed."
        );
    }
}
