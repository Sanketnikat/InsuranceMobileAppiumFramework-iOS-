package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC41_CopyPasteMobileNumberTest extends BaseTest {

    @Test
    public void verifyCopyOption() {

        // Open Login
        driver.findElement(
                AppiumBy.xpath("//*[@name='Login' or @label='Login']")
        ).click();

        // Find Mobile Number field
        WebElement mobileField = driver.findElement(
                AppiumBy.accessibilityId("Enter Mobile Number")
        );

        // Enter number
        mobileField.click();
        mobileField.sendKeys("8921639271");

        System.out.println("Number entered.");

        // Long press Mobile Number field
        new Actions(driver)
                .clickAndHold(mobileField)
                .pause(Duration.ofSeconds(2))
                .release()
                .perform();

        System.out.println("Long press completed.");

        // Print page source
        System.out.println("================================");
        System.out.println("PAGE SOURCE AFTER LONG PRESS");
        System.out.println("================================");

        System.out.println(driver.getPageSource());

        System.out.println("================================");
        System.out.println("CHECK FOR COPY MENU");
        System.out.println("================================");
    }
}