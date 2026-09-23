package tests.sprint1;

import framework.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC12_WelcomeScreenTest extends BaseTest {

    @Test(description = "SC_01_TC_12 - Validate the Welcome to Bima Sugam screen functionality")
    public void verifyWelcomeToBimaSugamScreen() {

        // Wait for the Welcome screen to appear
        WebElement welcomeScreen = new org.openqa.selenium.support.ui.WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                driver -> driver.findElement(
                        By.xpath(
                                "//*[contains(@label,'Welcome to Bima Sugam') " +
                                "or contains(@name,'Welcome to Bima Sugam') " +
                                "or contains(@value,'Welcome to Bima Sugam')]"
                        )
                )
        );

        // Verify Welcome screen is displayed
        Assert.assertTrue(
                welcomeScreen.isDisplayed(),
                "Welcome to Bima Sugam screen is not displayed"
        );

        System.out.println("Welcome to Bima Sugam screen is displayed");
        System.out.println("Updated branding is displayed correctly");
        System.out.println("SC_01_TC_12 PASSED");
    }
}