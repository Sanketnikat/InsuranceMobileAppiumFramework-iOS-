package tests.sprint5.bd2m609;

import framework.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

// TEMPORARY - captures real screens for fixing TS_01_TC_001. Delete after use.
public class ZZ_DiscoveryTest extends BaseTest {

    private static final Path OUT = Path.of(System.getProperty("discovery.dir", "target/discovery"));
    private int n = 0;
    private WebDriverWait wait;

    private void dump(String label) {
        try {
            Files.createDirectories(OUT);
            String name = String.format("%02d_%s", ++n, label.replaceAll("[^A-Za-z0-9]", "_"));
            Files.writeString(OUT.resolve(name + ".xml"), driver.getPageSource());
            Files.write(OUT.resolve(name + ".png"), driver.getScreenshotAs(OutputType.BYTES));
            System.out.println("DUMPED " + name);
        } catch (Exception e) {
            System.out.println("DUMP FAILED " + label + ": " + e);
        }
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) { }
    }

    private void tap(By by, String label) {
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
        System.out.println("TAPPED " + label);
        sleep(2500);
        dump("after_" + label);
    }

    private void pick(String field, String value) {
        tap(AppiumBy.accessibilityId(field), "open_" + field);

        List<WebElement> inputs = driver.findElements(By.xpath("//XCUIElementTypeTextField"));
        if (!inputs.isEmpty()) {
            inputs.get(0).click();
            inputs.get(0).sendKeys(value);
            sleep(4000);
            dump("typed_" + value);
        }

        String xp = "//*[not(self::XCUIElementTypeTextField) and @visible='true' and "
                + "contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'"
                + value.toLowerCase() + "')]";
        List<WebElement> opts = driver.findElements(By.xpath(xp));
        System.out.println("OPTIONS for " + value + ": " + opts.size());
        for (WebElement o : opts) {
            System.out.println("  OPT " + o.getTagName() + " name=" + o.getAttribute("name") + " y=" + o.getRect().getY());
        }

        if (opts.isEmpty()) {
            List<WebElement> forbidden = driver.findElements(AppiumBy.accessibilityId("Forbidden"));
            if (!forbidden.isEmpty()) {
                throw new IllegalStateException("Quote lookup returned Forbidden for '" + value + "'.");
            }
            throw new IllegalStateException("No selectable option found for '" + value + "'.");
        }

        WebElement best = opts.get(opts.size() - 1);
        System.out.println("PICKING " + best.getTagName() + " name=" + best.getAttribute("name"));
        best.click();
        sleep(2500);
        dump("picked_" + value);
    }

    @Test
    public void discover() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try {
            tap(AppiumBy.accessibilityId("Login"), "Login");
            tap(By.xpath("//*[@name='Private Bike Insurance']"), "PrivateBike");
            tap(By.xpath("//*[contains(@name,'Brand new bike')]"), "BrandNewBike");

            String[][] fields = {
                    {"Select Make & Model", "ADMS GTR"},
                    {"Select Fuel Type", "ELECTRIC"},
                    {"Select Variant", "STANDARD"},
                    {"Select Registration City", "Chittoor"},
                    {"Select Owner Type", "Individual"}
            };
            for (String[] f : fields) {
                pick(f[0], f[1]);
            }

            tap(AppiumBy.accessibilityId("View Prices"), "ViewPrices");
            tap(AppiumBy.accessibilityId("3 Years"), "3Years");
            tap(AppiumBy.accessibilityId("Confirm"), "Confirm");
            sleep(8000);
            dump("insurer_listing");

            // First price button on the listing
            List<WebElement> prices = driver.findElements(By.xpath(
                    "//XCUIElementTypeButton[contains(@name,'₹')]"));
            System.out.println("PRICE BUTTONS: " + prices.size());
            for (WebElement p : prices) {
                System.out.println("  PRICE " + p.getAttribute("name") + " y=" + p.getRect().getY());
            }
            if (!prices.isEmpty()) {
                prices.get(0).click();
                sleep(6000);
                dump("after_price");
            }

            tap(AppiumBy.accessibilityId("Confirm Plan"), "ConfirmPlan");
            sleep(5000);
            dump("preproposal");
            driver.executeScript("mobile: swipe", java.util.Map.of("direction", "up"));
            sleep(1500);
            dump("preproposal_scrolled");
            driver.executeScript("mobile: swipe", java.util.Map.of("direction", "up"));
            sleep(1500);
            dump("preproposal_scrolled2");
        } catch (Exception e) {
            System.out.println("DISCOVERY STOPPED: " + e.toString().split("\n")[0]);
            dump("failure");
        }
    }
}
