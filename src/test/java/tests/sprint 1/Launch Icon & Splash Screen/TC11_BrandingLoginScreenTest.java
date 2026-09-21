package tests.sprint1;

import framework.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC11_BrandingLoginScreenTest extends BaseTest {

    @Test(description = "SC_01_TC_011 - Verify branding updates on login screen")
    public void verifyLoginScreenBranding() {

        WebElement loginScreen = new org.openqa.selenium.support.ui.WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        ).until(
                driver -> driver.findElement(
                        By.xpath("//*[contains(@label,'Login') or contains(@name,'Login')]")
                )
        );

        Assert.assertTrue(
                loginScreen.isDisplayed(),
                "Login screen is not displayed"
        );

        System.out.println("Login screen displayed successfully");

        String pageSource = driver.getPageSource();

        Assert.assertTrue(
                pageSource.contains("Bima Sugam")
                        || pageSource.contains("BimaSugam"),
                "Bima Sugam branding is not displayed on Login screen"
        );

        System.out.println("Bima Sugam branding is displayed successfully");
        System.out.println("TC11 PASSED");
    }
}