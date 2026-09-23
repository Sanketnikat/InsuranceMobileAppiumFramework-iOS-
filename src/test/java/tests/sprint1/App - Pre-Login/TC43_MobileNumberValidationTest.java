package tests.sprint1;

import framework.BaseTest;
import locators.IOSLocators;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC43_MobileNumberValidationTest extends BaseTest {

    @Test(
    description = "BD2M-673 - TC02 - Mobile number accepts a maximum of 10 digits",
    groups = {"sprint1", "sprint2", "pre-login", "positive"}
    )
    public void TC02_mobileNumberMaximum10Digits() {

    LoginPage loginPage = new LoginPage(driver);

    step("Navigate to the Mobile Number field");
    loginPage.clickLogin();

    step("Enter a 10-digit mobile number");
    String tenDigitNumber = "8921639271";
    WebDriverWait wait = new WebDriverWait(
        driver,
        Duration.ofSeconds(20)
    );
        WebElement mobileNumberField = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            IOSLocators.MOBILE_NUMBER_INPUT
        )
        );

        mobileNumberField.click();
        new Actions(driver)
            .sendKeys(tenDigitNumber + "12")
            .perform();

        step("Observe that the Mobile Number field remains limited to 10 digits");

    step("Verify that the Mobile Number field contains no more than 10 digits");
        String pageSource = driver.getPageSource();

        Assert.assertTrue(
            pageSource.contains(tenDigitNumber),
            "The Mobile Number field did not contain the 10-digit number."
        );
        Assert.assertFalse(
            pageSource.contains(tenDigitNumber + "12"),
            "The Mobile Number field accepted more than 10 digits."
    );
    }

}