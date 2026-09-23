package tests.sprint1;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC32_PreLoginScreenFigmaDesignTest extends BaseTest {

    @Test
    public void TC32_verifyPreLoginScreen() {

        System.out.println("================================");
        System.out.println("TC32 - Pre-Login Screen");
        System.out.println("================================");

        String pageSource = driver.getPageSource();

        // Verify Welcome heading
        Assert.assertTrue(
                pageSource.contains("Welcome to Bima Sugam"),
                "Welcome to Bima Sugam is not displayed."
        );

        System.out.println("Welcome to Bima Sugam is displayed.");

        // Verify Login button
        Assert.assertTrue(
                pageSource.contains("Login"),
                "Login button is not displayed."
        );

        System.out.println("Login button is displayed.");

        // Verify main image/content
        Assert.assertTrue(
                pageSource.contains("XCUIElementTypeImage"),
                "Main image/content is not displayed."
        );

        System.out.println("Main image/content is displayed.");

        System.out.println(
                "SC_01_TC_032 PASSED - Pre-login screen content is displayed."
        );
    }
}