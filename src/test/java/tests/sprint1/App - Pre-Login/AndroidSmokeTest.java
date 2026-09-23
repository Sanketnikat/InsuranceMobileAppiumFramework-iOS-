package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AndroidSmokeTest extends BaseTest {

    @Test
    public void verifyBimaSugamAndroidAppLaunches() {

        System.out.println("================================");
        System.out.println("ANDROID SMOKE TEST");
        System.out.println("================================");

        String pageSource = driver.getPageSource();

        Assert.assertNotNull(
                pageSource,
                "Page source is not available."
        );

        Assert.assertTrue(
                pageSource.length() > 0,
                "Android app page source is empty."
        );

        System.out.println(
                "Android Bima Sugam app launched successfully."
        );
    }


    @Test
    public void enterLoginCredentials() {

        System.out.println("================================");
        System.out.println("ANDROID ENTER CREDENTIALS TEST");
        System.out.println("================================");

        // Step 1 - Click Login
        WebElement loginButton = driver.findElement(
                AppiumBy.accessibilityId("Login")
        );

        loginButton.click();

        System.out.println("Login button clicked.");

        // Step 2 - Enter Mobile Number
        WebElement mobileNumber = driver.findElement(
                AppiumBy.xpath(
                        "//android.widget.EditText[@hint='Enter Mobile Number']"
                )
        );

        mobileNumber.click();
        mobileNumber.sendKeys("8921639271");

        System.out.println("Mobile number entered.");

        // Step 3 - Enter Date of Birth
        WebElement dateOfBirth = driver.findElement(
                AppiumBy.xpath(
                        "//android.widget.EditText[@hint='Date of Birth']"
                )
        );

        dateOfBirth.click();
        dateOfBirth.sendKeys("01/01/1999");

        System.out.println("Date of Birth entered.");

        // Step 4 - Verify Login via OTP button
        WebElement loginViaOTP = driver.findElement(
                AppiumBy.accessibilityId("Login via OTP")
        );

        Assert.assertTrue(
                loginViaOTP.isDisplayed(),
                "Login via OTP button is not displayed."
        );

        System.out.println("Login via OTP button displayed.");

        System.out.println("================================");
        System.out.println("CREDENTIALS ENTERED SUCCESSFULLY");
        System.out.println("================================");
    }
}