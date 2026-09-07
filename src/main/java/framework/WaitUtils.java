package framework;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class WaitUtils {
    private static final Duration TIMEOUT = Duration.ofSeconds(20);

    private WaitUtils() {}

    public static void visible(AppiumDriver driver, By locator) {
        new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void clickable(AppiumDriver driver, By locator) {
        new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean isVisible(AppiumDriver driver, By locator) {
        try {
            visible(driver, locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
