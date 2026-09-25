package tests.pwa;

import framework.BaseTest;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class PwaLoginPageLoadTest extends BaseTest {

    @Test(
            description = "Validate that the Bima Sugam PWA login page loads on BrowserStack mobile web",
            groups = {"pwa", "smoke"}
    )
    public void pwaLoginPageLoads() {
        WebDriverWait wait = new WebDriverWait(sessionDriver, Duration.ofSeconds(30));

        wait.until(webDriver -> webDriver.getCurrentUrl().contains("marketplace-test.bsifinternal.com"));

        String pageSource = sessionDriver.getPageSource().toLowerCase();
        Assert.assertTrue(
                pageSource.contains("login") || pageSource.contains("mobile"),
                "The PWA login page content was not detected."
        );
    }
}
