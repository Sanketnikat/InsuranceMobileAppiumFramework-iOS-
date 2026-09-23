package tests.sprint1;

import framework.BaseTest;
import locators.IOSLocators;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC13_DateOfBirthLabelTest extends BaseTest {

    @Test(
            description = "TC13 - Verify Date of Birth / Date of Incorporation label",
            groups = {"sprint1", "sprint2", "pre-login", "positive"}
    )
    public void TC13_verifyDateOfBirthLabel() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        LoginPage loginPage = new LoginPage(driver);

        // Tap Login
        loginPage.clickLogin();

        // Wait for Date of Birth field
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        IOSLocators.DOB_INPUT
                )
        );

        // Verify Date of Birth label
        boolean labelDisplayed = driver.getPageSource()
                .contains("Date of Birth");

        Assert.assertTrue(
                labelDisplayed,
                "Date of Birth label is not displayed."
        );

        System.out.println(
                "SC_01_TC_013 PASSED - Date of Birth label is displayed correctly"
        );
    }
}