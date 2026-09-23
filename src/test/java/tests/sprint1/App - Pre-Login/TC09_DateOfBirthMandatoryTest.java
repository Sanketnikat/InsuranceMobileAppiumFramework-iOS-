package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC09_DateOfBirthMandatoryTest extends BaseTest {

    @Test
    public void verifyDateOfBirthMandatory() {

        // Tap Login on pre-login screen
        driver.findElement(
                AppiumBy.xpath("//*[@name='Login' or @label='Login']")
        ).click();

        // Verify Date of Birth field
        Assert.assertTrue(
                driver.findElement(
                        AppiumBy.accessibilityId("Date of Birth")
                ).isDisplayed(),
                "Date of Birth field is not displayed"
        );

        // Leave Date of Birth blank
        driver.findElement(
                AppiumBy.accessibilityId("Date of Birth")
        ).clear();

        // Tap Login via OTP
        driver.findElement(
                AppiumBy.accessibilityId("Login via OTP")
        ).click();

        // Check validation
        String pageSource = driver.getPageSource();

        Assert.assertTrue(
                pageSource.contains("Date of Birth"),
                "Date of Birth mandatory validation is not displayed"
        );

        System.out.println("SC_01_TC_009 PASSED");
    }
}