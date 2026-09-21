package tests.sprint1;

import framework.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC14_RapidLoginTapTest extends BaseTest {

    @Test(description = "SC_01_TC_14 - Verify multiple rapid taps on Login")
    public void verifyRapidLoginTaps() throws InterruptedException {

        // Wait for Login button to be displayed
        WebElement loginButton = new org.openqa.selenium.support.ui.WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                driver -> driver.findElement(
                        By.xpath("//*[contains(@label,'Login') or contains(@name,'Login')]")
                )
        );

        // Verify Login button is displayed
        Assert.assertTrue(
                loginButton.isDisplayed(),
                "Login button is not displayed"
        );

        System.out.println("Login button displayed");

        // Rapidly tap Login multiple times
        for (int i = 0; i < 5; i++) {
            loginButton.click();
        }

        System.out.println("Login tapped 5 times rapidly");

        // Wait for navigation/action to complete
        Thread.sleep(2000);

        // Verify application is still running
        Assert.assertNotNull(
                driver.getPageSource(),
                "Application crashed or UI is unavailable"
        );

        System.out.println("Application is stable after rapid taps");

        System.out.println("SC_01_TC_14 PASSED");
    }
}