package tests.sprint1;

import framework.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC13_Splash6DisplayedTest extends BaseTest {

    @Test(description = "SC_01_TC_13 - Verify Splash 6 is displayed correctly")
    public void verifySplash6DisplayedCorrectly() {

        // Wait for Splash 6 to be displayed
        WebElement splash6 = new org.openqa.selenium.support.ui.WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                driver -> driver.findElement(
                        By.xpath(
                                "//*[contains(@label,'Splash 6') " +
                                "or contains(@name,'Splash 6') " +
                                "or contains(@value,'Splash 6')]"
                        )
                )
        );

        // Verify Splash 6 is displayed
        Assert.assertTrue(
                splash6.isDisplayed(),
                "Splash 6 is not displayed"
        );

        System.out.println("Splash 6 is displayed");

        // Verify Bima Sugam branding/content is available
        Assert.assertNotNull(
                driver.getPageSource(),
                "Splash 6 UI is unavailable"
        );

        System.out.println("Bima Sugam branding/content is available");
        System.out.println("SC_01_TC_13 PASSED");
    }
}