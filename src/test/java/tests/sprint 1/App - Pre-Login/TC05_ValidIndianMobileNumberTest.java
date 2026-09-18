package tests.sprint1;

import framework.BaseTest;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC05_ValidIndianMobileNumberTest extends BaseTest {

    @Test(
            description = "BD2M-676 - TC05 - Accept a valid 10-digit Indian mobile number",
            groups = {"sprint1", "sprint2", "pre-login", "positive"}
    )
    public void TC05_validIndianMobileNumberIsAccepted() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );
        String validIndianMobileNumber = "8921639271";

        LoginPage loginPage = new LoginPage(driver);

        step("Tap Login to open the pre-login form");
        loginPage.clickLogin();

        step("Enter a valid 10-digit Indian mobile number");
        loginPage.enterMobileNumber(validIndianMobileNumber);

        step("Verify that the valid mobile number is accepted");
        Assert.assertTrue(
                wait.until(driver -> driver.getPageSource().contains(
                        validIndianMobileNumber
                )),
                "The valid 10-digit Indian mobile number was not accepted."
        );

        Assert.assertFalse(
                driver.getPageSource().contains("Invalid mobile number"),
                "The valid Indian mobile number was marked invalid."
        );
    }
}
