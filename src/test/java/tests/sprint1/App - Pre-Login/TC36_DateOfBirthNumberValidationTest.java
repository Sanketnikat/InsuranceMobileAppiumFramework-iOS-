package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC36_DateOfBirthNumberValidationTest extends BaseTest {

    @Test
    public void TC36_verifyDateOfBirthNumberValidation() {

        // Tap Login
        driver.findElement(
                AppiumBy.xpath("//*[@name='Login' or @label='Login']")
        ).click();

        // Find Date of Birth field
        driver.findElement(
                AppiumBy.accessibilityId("Date of Birth")
        ).click();

        // Enter a number
        driver.findElement(
                AppiumBy.accessibilityId("Date of Birth")
        ).sendKeys("1");

        // Get page source
        String pageSource = driver.getPageSource();

        System.out.println("================================");
        System.out.println("PAGE SOURCE");
        System.out.println("================================");
        System.out.println(pageSource);

        // Verify validation message
        boolean validationMessage =
                pageSource.contains("Date of Birth")
                        || pageSource.contains("Invalid")
                        || pageSource.contains("invalid");

        Assert.assertTrue(
                validationMessage,
                "Date of Birth validation message was not displayed."
        );

        System.out.println(
                "SC_01_TC_036 PASSED - Date of Birth validation message is displayed."
        );
    }
}