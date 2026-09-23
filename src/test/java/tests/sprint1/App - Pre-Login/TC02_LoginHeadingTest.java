package tests.sprint1;

import framework.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC02_LoginHeadingTest extends BaseTest {

    @Test(description = "SC_01_TC_002 - Verify that the login card displays the Login heading")
    public void verifyLoginHeadingDisplayed() {

        System.out.println("========================================");
        System.out.println("SC_01_TC_002 - Login Heading");
        System.out.println("========================================");

        System.out.println("Launching Bima Sugam application");

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        /*
         * Wait for the Login heading on the pre-login screen.
         */
        WebElement loginHeading = wait.until(
                driver -> driver.findElement(
                        By.xpath(
                                "//*[normalize-space(@label)='Login' " +
                                "or normalize-space(@name)='Login' " +
                                "or normalize-space(@value)='Login']"
                        )
                )
        );

        Assert.assertTrue(
                loginHeading.isDisplayed(),
                "Login heading is not displayed on the login card"
        );

        System.out.println("Pre-login screen displayed");
        System.out.println("Login heading is displayed on the login card");

        System.out.println("SC_01_TC_002 PASSED");
    }
}