package tests.sprint1;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC33_PreLoginContentNoClippingTest extends BaseTest {

    @Test
    public void TC33_verifyPreLoginContentIsNotClipped() {

        System.out.println("================================");
        System.out.println("TC33 - Pre-Login Content Clipping");
        System.out.println("================================");

        // Verify Welcome heading
        WebElement welcomeText = driver.findElement(
                AppiumBy.accessibilityId("Welcome to Bima Sugam")
        );

        Assert.assertTrue(
                welcomeText.isDisplayed(),
                "Welcome to Bima Sugam is not displayed."
        );

        System.out.println("Welcome to Bima Sugam is displayed.");

        // Find Login button
        WebElement loginButton = driver.findElement(
                AppiumBy.xpath("//*[@name='Login' or @label='Login']")
        );

        Assert.assertTrue(
                loginButton.isDisplayed(),
                "Login button is not displayed."
        );

        System.out.println("Login button is displayed.");

        // Verify Login button is inside the screen
        int screenWidth = driver.manage()
                .window()
                .getSize()
                .getWidth();

        int screenHeight = driver.manage()
                .window()
                .getSize()
                .getHeight();

        int loginX = loginButton.getLocation().getX();
        int loginY = loginButton.getLocation().getY();

        int loginWidth = loginButton.getSize().getWidth();
        int loginHeight = loginButton.getSize().getHeight();

        System.out.println("Screen Width  : " + screenWidth);
        System.out.println("Screen Height : " + screenHeight);

        System.out.println("Login X       : " + loginX);
        System.out.println("Login Y       : " + loginY);
        System.out.println("Login Width   : " + loginWidth);
        System.out.println("Login Height  : " + loginHeight);

        Assert.assertTrue(
                loginX >= 0,
                "Login button is clipped on the left side."
        );

        Assert.assertTrue(
                loginY >= 0,
                "Login button is clipped at the top."
        );

        Assert.assertTrue(
                loginX + loginWidth <= screenWidth,
                "Login button is clipped on the right side."
        );

        Assert.assertTrue(
                loginY + loginHeight <= screenHeight,
                "Login button is clipped at the bottom."
        );

        System.out.println(
                "SC_01_TC_033 PASSED - Pre-login content is displayed without clipping."
        );
    }
}