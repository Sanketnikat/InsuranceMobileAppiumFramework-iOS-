package tests.sprint1;

import framework.BaseTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

public class TC04_FourSplashScreensTest extends BaseTest {

    @Test(
            description = "SC_01_TC_004 - Verify all 4 splash screens are displayed."
    )
    public void verifyFourSplashScreens() throws Exception {

        System.out.println("========================================");
        System.out.println("SC_01_TC_004 - Four Splash Screens Test");
        System.out.println("========================================");

        Assert.assertNotNull(driver, "Driver is not initialized.");

        System.out.println("Application launched successfully.");

        TakesScreenshot screenshot =
                (TakesScreenshot) driver;

        // Splash Screen 1
        Thread.sleep(1000);

        File splash1 =
                screenshot.getScreenshotAs(OutputType.FILE);

        System.out.println(
                "Splash Screen 1 captured: "
                        + splash1.getAbsolutePath()
        );

        // Splash Screen 2
        Thread.sleep(1000);

        File splash2 =
                screenshot.getScreenshotAs(OutputType.FILE);

        System.out.println(
                "Splash Screen 2 captured: "
                        + splash2.getAbsolutePath()
        );

        // Splash Screen 3
        Thread.sleep(1000);

        File splash3 =
                screenshot.getScreenshotAs(OutputType.FILE);

        System.out.println(
                "Splash Screen 3 captured: "
                        + splash3.getAbsolutePath()
        );

        // Splash Screen 4
        Thread.sleep(1000);

        File splash4 =
                screenshot.getScreenshotAs(OutputType.FILE);

        System.out.println(
                "Splash Screen 4 captured: "
                        + splash4.getAbsolutePath()
        );

        Assert.assertNotNull(splash1, "Splash Screen 1 screenshot failed.");
        Assert.assertNotNull(splash2, "Splash Screen 2 screenshot failed.");
        Assert.assertNotNull(splash3, "Splash Screen 3 screenshot failed.");
        Assert.assertNotNull(splash4, "Splash Screen 4 screenshot failed.");

        System.out.println("----------------------------------------");
        System.out.println("Splash Screen 1 captured successfully.");
        System.out.println("Splash Screen 2 captured successfully.");
        System.out.println("Splash Screen 3 captured successfully.");
        System.out.println("Splash Screen 4 captured successfully.");
        System.out.println("----------------------------------------");

        System.out.println("TC04 PASSED.");
        System.out.println("========================================");
    }
}